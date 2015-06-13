package org.moflon.ide.ui.admin.wizards.mosl;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.moflon.ide.ui.UIActivator;

/**
 * Used by {@link NewEPackageWizard}. Contains all GUI controls for the wizard page.
 */
public class NewTGGPage extends WizardPage
{
   private String packageName;
   private String sourceLanguage;
   private String targetLanguage;
   
   public NewTGGPage()
   {
      super("NewTgg");
      packageName = "ExampleIntegration";
      sourceLanguage = "";
      targetLanguage = "";

      // Set information on the page
      setTitle("New Tgg");
      setDescription("This wizard creates a new TGG integration package for the MOSL project.");
      setImageDescriptor(UIActivator.getImage("resources/icons/metamodelProjectWizard.gif"));
   }

   @Override
   public void createControl(Composite parent)
   {
      // Create root container
      Composite container = new Composite(parent, SWT.NULL);
      GridLayout layout = new GridLayout();
      container.setLayout(layout);
      layout.numColumns = 2;

      // Create control for entering project name
      Label label = new Label(container, SWT.NULL);
      label.setText("&Package name:");

      final Text packageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      packageNameText.setLayoutData(gd);
      
      packageNameText.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e)
         {
            packageName = packageNameText.getText();
            dialogChanged();
         }
      });

      // Create control for entering project name
      label = new Label(container, SWT.NULL);
      label.setText("&Source language:");

      final Text sourceLanguageText = new Text(container, SWT.BORDER | SWT.SINGLE);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      sourceLanguageText.setLayoutData(gd);
      
      sourceLanguageText.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e)
         {
            sourceLanguage = sourceLanguageText.getText();
            dialogChanged();
         }
      });

      // Create control for entering project name
      label = new Label(container, SWT.NULL);
      label.setText("&Target language:");

      final Text targetLanguageText = new Text(container, SWT.BORDER | SWT.SINGLE);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      targetLanguageText.setLayoutData(gd);
      
      targetLanguageText.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e)
         {
            targetLanguage = targetLanguageText.getText();
            dialogChanged();
         }
      });

      // Set controls and update
      setControl(container);
      dialogChanged();
   }

   public String getPackageName()
   {
      return packageName;
   }
   
   public String getSourceLanguage()
   {
      return sourceLanguage;
   }
   
   public String getTargetLanguage()
   {
      return targetLanguage;
   }
   
   @Override
   public boolean canFlipToNextPage()
   {
      return super.canFlipToNextPage() && getErrorMessage() == null;
   }

   private void dialogChanged()
   {
   }

}
