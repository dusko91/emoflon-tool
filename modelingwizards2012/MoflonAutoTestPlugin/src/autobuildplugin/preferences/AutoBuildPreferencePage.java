package autobuildplugin.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import autobuildplugin.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class AutoBuildPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public AutoBuildPreferencePage() {
		super(GRID);
		init(PlatformUI.getWorkbench());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		FileFieldEditor filter = new FileFieldEditor(
				PreferenceConstants.AB_PROJECTSET, "ProjectSet:",
				getFieldEditorParent());
		String[] ext = { "*.psf" };
		filter.setFileExtensions(ext);
		addField(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.AB_NEXTOP,
				PreferenceConstants.AB_DEFAULT_NEXTOP);
		store.setDefault(PreferenceConstants.AB_PROJECTSET,
				PreferenceConstants.AB_DEFAULT_PROJECTSET);
		setDescription("AutoBuild Plugin Preferences");
		setPreferenceStore(store);
	}
}