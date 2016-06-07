package org.moflon.tgg.algorithm.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.moflon.tgg.runtime.EMoflonEdge;
import org.moflon.tgg.runtime.RuntimeFactory;

import gnu.trove.TIntCollection;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

/**
 * Represents a set of matches and precedence dependencies between matches.
 * 
 * @author anjorin
 *
 */
public abstract class PrecedenceStructure<M> {

	protected TIntObjectHashMap<M> matches = new TIntObjectHashMap<>();

	protected HashMap<EObject, TIntArrayList> contextToMatch = new HashMap<>();
	protected HashMap<EObject, TIntArrayList> createToMatch = new HashMap<>();

	protected TIntObjectHashMap<TIntHashSet> matchToChildren = new TIntObjectHashMap<>();
	protected TIntObjectHashMap<TIntHashSet> matchToParents = new TIntObjectHashMap<>();

	protected void calculateTables(M match) {
		
		matches.put(matchToInt(match), match);

		getCreatedElements(match).forEach(elt -> addMatchToCreateTable(elt, matchToInt(match)));
		getContextElements(match).forEach(elt -> addMatchToContextTable(elt, matchToInt(match)));

		TIntHashSet children = extendChildrenTable(match);
		TIntHashSet parents = extendParentsTable(match);

		children.forEach(c -> {
			if(matchToParents.contains(c))
				matchToParents.get(c).add(matchToInt(match));
			return true;
		});

		parents.forEach(p -> {
			if(matchToChildren.contains(p))
				matchToChildren.get(p).add(matchToInt(match));
			return true;
		});
	}

	// -------

	private void addMatchToContextTable(EObject element, int id) {
		addMatchToTable(contextToMatch, element, id);
	}

	private void addMatchToCreateTable(EObject element, int id) {
		addMatchToTable(createToMatch, element, id);
	}

	private void addMatchToTable(HashMap<EObject, TIntArrayList> table, EObject element, int id) {
		if (!table.containsKey(element))
			table.put(element, new TIntArrayList());

		table.get(element).add(id);
	}

	// --------

	private TIntHashSet extendChildrenTable(M match) {
		return extendTable(match, getCreatedElements(match), contextToMatch, matchToChildren);
	}

	private TIntHashSet extendParentsTable(M match) {
		return extendTable(match, getContextElements(match), createToMatch, matchToParents);
	}

	private TIntHashSet extendTable(M match, Collection<EObject> elements,
			HashMap<EObject, TIntArrayList> eltToMatches, TIntObjectHashMap<TIntHashSet> matchTable) {
		TIntHashSet table = new TIntHashSet();
		for (EObject elt : elements) {
			table.addAll(getOrReturnEmpty(elt, eltToMatches));
			table.remove(matchToInt(match));
		}
		matchTable.put(matchToInt(match), table);
		return table;
	}

	protected TIntArrayList getOrReturnEmpty(EObject elt, HashMap<EObject, TIntArrayList> table) {
		if (table.containsKey(elt))
			return table.get(elt);
		else
			return new TIntArrayList();
	}

	// ---------

	public TIntCollection children(int m) {
		if (!matchToChildren.containsKey(m))
			return new TIntHashSet();
		return matchToChildren.get(m);
	}

	public TIntCollection parents(int m) {
		if (!matchToParents.containsKey(m))
			return new TIntHashSet();
		return matchToParents.get(m);
	}

	// ----------

	public Stream<M> creates(Graph elements) {
		return elements.stream().flatMap(e -> getCreatingMatches(e).stream());
	}
	
	public Stream<M> creates(EObject elt) {
		return getCreatingMatches(elt).stream();
	}

	public Collection<M> getCreatingMatches(EObject o) {
		if (createToMatch.containsKey(o))
			return getAsCollection(createToMatch.get(o));
		else
			return Collections.emptyList();
	}

	public Collection<M> getContextMatches(EObject o) {
		if (contextToMatch.containsKey(o))
			return getAsCollection(contextToMatch.get(o));
		else
			return Collections.emptyList();
	}

	public Collection<M> getMatches() {
		return new ArrayList<M>(matches.valueCollection());
	}
	
	public TIntCollection getMatchIDs(){
		return new TIntHashSet(matches.keySet());
	}

	public abstract Collection<EObject> getContextElements(M m);

	public abstract Collection<EObject> getCreatedElements(M m);

	// ----------

	public org.moflon.tgg.runtime.PrecedenceStructure save() {
		org.moflon.tgg.runtime.PrecedenceStructure ps = RuntimeFactory.eINSTANCE.createPrecedenceStructure();

		HashMap<M, org.moflon.tgg.runtime.TripleMatch> conversionTable = convertToMatches();
		ps.getTripleMatches().addAll(conversionTable.values().stream().sorted((a, b) -> a.getNumber() - b.getNumber())
				.collect(Collectors.toList()));
		
		getMatchIDs().forEach(m -> {
			children(m).forEach(child -> {
				if (conversionTable.containsKey(child))
					conversionTable.get(intToMatch(m)).getChildren().add(conversionTable.get(intToMatch(child)));
				return true;
			});
			return true;
		});

		return ps;
	}

	private HashMap<M, org.moflon.tgg.runtime.TripleMatch> convertToMatches() {
		HashMap<M, org.moflon.tgg.runtime.TripleMatch> conversionTable = new HashMap<>();
		getMatches().forEach(m -> conversionTable.put(m, toEMF(m)));

		return conversionTable;
	}

	protected void addEdges(org.moflon.tgg.runtime.TripleMatch tripleMatch) {
		tripleMatch.getContextElements().forEach(elt -> {
			addIfEdge(tripleMatch, elt);
		});

		tripleMatch.getCreatedElements().forEach(elt -> {
			addIfEdge(tripleMatch, elt);
		});

		tripleMatch.getSourceElements().forEach(elt -> {
			addIfEdge(tripleMatch, elt);
		});

		tripleMatch.getTargetElements().forEach(elt -> {
			addIfEdge(tripleMatch, elt);
		});

		tripleMatch.getCorrespondenceElements().forEach(elt -> {
			addIfEdge(tripleMatch, elt);
		});
	}

	private void addIfEdge(org.moflon.tgg.runtime.TripleMatch tripleMatch, EObject elt) {
		if (elt instanceof EMoflonEdge)
			tripleMatch.getContainedEdges().add(elt);
	}

	public Collection<M> createsAsCollection(EObject elt) {
		return getAsCollection(createToMatch.get(elt));
	}

	protected abstract org.moflon.tgg.runtime.TripleMatch toEMF(M m);

	protected abstract M fromEMF(org.moflon.tgg.runtime.TripleMatch m);

	public M intToMatch(int id) {
		if(!matches.containsKey(id))
			throw new RuntimeException("never seen that match id before");
		return matches.get(id);
	}
	
	public int matchToInt(M m){
		return m.hashCode();
	}

	public Collection<M> getAsCollection(TIntCollection tIntCollection) {
		ArrayList<M> result = new ArrayList<>();
		tIntCollection.forEach(i -> {
			result.add(intToMatch(i));
			return true;
		});
		return result;
	}
}
