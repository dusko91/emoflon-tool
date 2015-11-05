/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl;

import org.eclipse.xtext.scoping.IScopeProvider;
import org.moflon.tgg.mosl.scoping.TGGImportedNamespaceAwareLocalScopeProvider;
import org.moflon.tgg.mosl.scoping.TGGScopeProvider;

import com.google.inject.Binder;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class TGGRuntimeModule extends org.moflon.tgg.mosl.AbstractTGGRuntimeModule {

	@Override
	public Class<? extends IScopeProvider> bindIScopeProvider() {
		return TGGScopeProvider.class;
	}
	
	@Override
	public void configureIScopeProviderDelegate(com.google.inject.Binder binder){
		binder.bind(org.eclipse.xtext.scoping.IScopeProvider.class).annotatedWith(com.google.inject.name.Names.
				named(org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).
			to(TGGImportedNamespaceAwareLocalScopeProvider.class);
	}
	
//	@Override
//	public void configureSerializerIScopeProvider(Binder binder) {
//		binder.bind(org.eclipse.xtext.scoping.IScopeProvider.class).annotatedWith(org.eclipse.xtext.serializer.tokens.SerializerScopeProviderBinding.class).to(TGGScopeProvider.class);
//	}
	
}
