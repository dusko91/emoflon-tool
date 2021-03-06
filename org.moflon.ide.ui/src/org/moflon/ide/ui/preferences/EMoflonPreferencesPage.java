package org.moflon.ide.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.moflon.ide.ui.UIActivator;

/**
 * This {@link PreferencePage} holds the eMoflon-specific configuration options
 * 
 * @author Roland Kluge - Initial implementation
 *
 */
public class EMoflonPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage
{

   /**
    * Default color that should be used when creating buttons, labels, etc.
    */
   private static final Color DEFAULT_BACKGROUND_COLOR = null;

   /**
    * Holds the validation timeout (in seconds)
    */
   private Text validationTimeoutTextBox;

   /**
    * Used to enable/disable reachability analysis (e.g., see org.moflon.democles.reachability.javabdd.BDDReachabilityAnalyzer)
    */
   private Button reachabilityAnalysisEnabledButton;

   /**
    * Used to fine-tune reachability analysis.
    * The larger the maximum adornment size, the longer the reachability analysis will take
    */
   private Text reachabilityAnalysisMaxAdornmentSizeTextBox;

   /**
    * Cached error messages
    */
   private List<String> currentErrorMessages;

   public EMoflonPreferencesPage()
   {
      // Required by OSGi framework
      currentErrorMessages = new ArrayList<>();
   }

   @Override
   public void init(final IWorkbench workbench)
   {
      // nop
   }

   @Override
   public boolean performOk()
   {
      storeValues();
      return super.performOk();
   }

   @Override
   protected void performDefaults()
   {
      super.performDefaults();
      EMoflonPreferenceInitializer.restoreDefaults();
      initializeValues();
   }

   @Override
   protected void performApply()
   {
      super.performApply();
      if (checkValues())
         storeValues();
   }

   @Override
   protected Control createContents(final Composite parent)
   {
      final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
      {
         final Label pageDescriptionLabel = toolkit.createLabel(parent, "eMoflon Preferences");
         pageDescriptionLabel.setBackground(DEFAULT_BACKGROUND_COLOR);
         final GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
         pageDescriptionLabel.setLayoutData(gd1);
      }

      createAndAddValidationComponents(parent, toolkit);

      createAndAddReachabilityAnalysisComponents(parent, toolkit);

      initializeValues();

      return new Composite(parent, SWT.NULL);
   }

   @Override
   protected IPreferenceStore doGetPreferenceStore()
   {
      return EMoflonPreferenceInitializer.getPreferencesStore();
   }

   private void createAndAddValidationComponents(final Composite parent, final FormToolkit toolkit)
   {
      final Composite validationTimeoutComponent = toolkit.createComposite(parent);
      validationTimeoutComponent.setBackground(DEFAULT_BACKGROUND_COLOR);
      final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
      validationTimeoutComponent.setLayoutData(gridData);
      validationTimeoutComponent.setLayout(new GridLayout(2, true));

      final Label validationTimeoutLabel = toolkit.createLabel(validationTimeoutComponent, "Validation timeout in seconds: ");
      validationTimeoutLabel.setBackground(DEFAULT_BACKGROUND_COLOR);

      this.validationTimeoutTextBox = toolkit.createText(validationTimeoutComponent, "");
      final GridData gd5 = new GridData(GridData.FILL_HORIZONTAL);
      this.validationTimeoutTextBox.setLayoutData(gd5);
      this.validationTimeoutTextBox.setToolTipText("The maximum time that the validation may take. Choose '0' to wait undefinitely long.");
      this.validationTimeoutTextBox.addFocusListener(new FocusListener() {

         private static final String ERROR_MSG = "Validation timeout must be an integer.";

         @Override
         public void focusLost(FocusEvent e)
         {
            if (!checkValidationTimeout())
            {
               currentErrorMessages.add(ERROR_MSG);
            } else
            {
               currentErrorMessages.remove(ERROR_MSG);
            }
            updateErrorMessage();
         }

         @Override
         public void focusGained(FocusEvent e)
         {
            // nop
         }
      });
   }

   private void createAndAddReachabilityAnalysisComponents(final Composite parent, final FormToolkit toolkit)
   {
      final Composite reachabilityAnalysisComponent = toolkit.createComposite(parent);
      reachabilityAnalysisComponent.setBackground(DEFAULT_BACKGROUND_COLOR);
      final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
      reachabilityAnalysisComponent.setLayoutData(gridData);
      reachabilityAnalysisComponent.setLayout(new GridLayout(2, true));

      final Label reachabilityAnalysisLabel = toolkit.createLabel(reachabilityAnalysisComponent, "Enable reachability analysis: ");
      reachabilityAnalysisLabel.setBackground(DEFAULT_BACKGROUND_COLOR);

      this.reachabilityAnalysisEnabledButton = toolkit.createButton(reachabilityAnalysisComponent, null, SWT.CHECK);
      this.reachabilityAnalysisEnabledButton.setBackground(DEFAULT_BACKGROUND_COLOR);

      final GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
      this.reachabilityAnalysisEnabledButton.setLayoutData(gridData2);
      this.reachabilityAnalysisEnabledButton.setToolTipText("Toggle checkbox to enable reachability analysis.");

      final Label maxAdornmentSizeLabel = toolkit.createLabel(reachabilityAnalysisComponent, "Reachability: Max. adornment size: ");
      maxAdornmentSizeLabel.setBackground(DEFAULT_BACKGROUND_COLOR);

      this.reachabilityAnalysisMaxAdornmentSizeTextBox = toolkit.createText(reachabilityAnalysisComponent, "");
      final GridData gd5 = new GridData(GridData.FILL_HORIZONTAL);
      this.reachabilityAnalysisMaxAdornmentSizeTextBox.setLayoutData(gd5);
      this.reachabilityAnalysisMaxAdornmentSizeTextBox.setToolTipText("The maximum size of adornments to. Choose '0' to use the maximum.");
      this.reachabilityAnalysisMaxAdornmentSizeTextBox.addFocusListener(new FocusListener() {

         private static final String ERROR_MSG = "The maximum adornment size must be an integer.";

         @Override
         public void focusLost(FocusEvent e)
         {
            if (!checkReachabilityAnalysisMaxAdornmentSize())
            {
               currentErrorMessages.add(ERROR_MSG);
            } else
            {
               currentErrorMessages.remove(ERROR_MSG);
            }

            updateErrorMessage();
         }

         @Override
         public void focusGained(FocusEvent e)
         {
            // nop
         }
      });
   }

   /**
    * This method reads the currently cached error messages ({@link #currentErrorMessages}), and invokes {@link #setErrorMessage(String)} and {@link #setValid(boolean)} appropriately
    */
   private void updateErrorMessage()
   {
      if (this.currentErrorMessages.isEmpty())
      {
         this.setErrorMessage(null);
         this.setValid(true);
      } else
      {
         this.setValid(false);
         this.setErrorMessage(StringUtils.join(this.currentErrorMessages, "\n"));
      }
   }

   private boolean checkValues()
   {
      return this.checkValidationTimeout() && this.checkReachabilityAnalysisMaxAdornmentSize();
   }

   /**
    * Validate content of {@link #reachabilityAnalysisMaxAdornmentSizeTextBox}
    */
   private boolean checkReachabilityAnalysisMaxAdornmentSize()
   {
      try
      {
         Integer.parseInt(reachabilityAnalysisMaxAdornmentSizeTextBox.getText());
         return true;
      } catch (NumberFormatException e)
      {
         return false;
      }
   }

   /**
    * Validate content of {@link #validationTimeoutTextBox}
    */
   private boolean checkValidationTimeout()
   {
      try
      {
         Integer.parseInt(validationTimeoutTextBox.getText());
         return true;
      } catch (NumberFormatException e)
      {
         return false;
      }
   }

   /**
    * Store current values shown on the page
    */
   private void storeValues()
   {

      EMoflonPreferenceInitializer.setValidationTimeoutMillis(Integer.parseInt(this.validationTimeoutTextBox.getText()) * 1000);
      EMoflonPreferenceInitializer.setReachabilityEnabled(this.reachabilityAnalysisEnabledButton.getSelection());
      EMoflonPreferenceInitializer.setReachabilityMaxAdornmentSize(Integer.parseInt(this.reachabilityAnalysisMaxAdornmentSizeTextBox.getText()));

      UIActivator.synchronizeEMoflonPreferencesStorage();
   }

   /**
    * Read stored values from {@link EMoflonPreferenceInitializer}
    */
   private void initializeValues()
   {
      this.validationTimeoutTextBox.setText(Integer.toString(EMoflonPreferenceInitializer.getValidationTimeoutMillis() / 1000));
      this.reachabilityAnalysisEnabledButton.setSelection(EMoflonPreferenceInitializer.getReachabilityEnabled());
      this.reachabilityAnalysisMaxAdornmentSizeTextBox.setText(Integer.toString(EMoflonPreferenceInitializer.getReachabilityMaxAdornmentSize()));

      UIActivator.synchronizeEMoflonPreferencesStorage();
   }
}
