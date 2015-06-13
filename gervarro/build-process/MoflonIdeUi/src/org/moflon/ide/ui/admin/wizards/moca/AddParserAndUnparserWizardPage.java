package org.moflon.ide.ui.admin.wizards.moca;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class AddParserAndUnparserWizardPage extends WizardPage
{
   private String fileExtension = "";

   private TechnologyButtonGroup parserTechnologyButtons;

   private TechnologyButtonGroup unparserTechnologyButtons;

   private Button createParserButton;

   private Button createUnparserButton;

   public AddParserAndUnparserWizardPage()
   {
      super("AddParserWizardPage");
      setTitle("Add Parser to Project.");
      setDescription("This wizard adds a parser to the project, using the specified technology.");
   }

   @Override
   public void createControl(Composite parent)
   {
      int numColumns = 3;

      // root container
      final Composite container = new Composite(parent, SWT.NULL);
      GridLayout layout = new GridLayout();
      layout.numColumns = numColumns;
      layout.horizontalSpacing = 20;
      container.setLayout(layout);

      fileExtensionRow(container);
      new Separator(SWT.SEPARATOR | SWT.HORIZONTAL).doFillIntoGrid(container, numColumns);
      parserRow(container);
      new Separator(SWT.SEPARATOR | SWT.HORIZONTAL).doFillIntoGrid(container, numColumns);
      unparserRow(container);
      new Separator(SWT.SEPARATOR | SWT.HORIZONTAL).doFillIntoGrid(container, numColumns);

      // Set controls and update
      setControl(container);
      setPageComplete(false);
   }

   private void parserRow(Composite container)
   {
      // parser technology
      Label parserTechnologyLabel = new Label(container, SWT.NULL);
      parserTechnologyLabel.setText("Parser technology:");
      parserTechnologyLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

      parserTechnologyButtons = new TechnologyButtonGroup(container);

      createParserButton = new Button(container, SWT.CHECK);
      createParserButton.setText("Create Parser");
      createParserButton.setSelection(true);
      createParserButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
      createParserButton.addListener(SWT.Selection, new Listener() {
         public void handleEvent(Event event)
         {
            updateStatus();
            if (createParserButton.getSelection())
            {
               parserTechnologyButtons.setEnabled(true);
            } else
            {
               parserTechnologyButtons.setEnabled(false);
            }
         }

      });
   }

   private void unparserRow(Composite container)
   {
      // parser technology
      Label parserTechnologyLabel = new Label(container, SWT.NULL);
      parserTechnologyLabel.setText("Unparser technology:");
      parserTechnologyLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

      unparserTechnologyButtons = new TechnologyButtonGroup(container);

      createUnparserButton = new Button(container, SWT.CHECK);
      createUnparserButton.setText("Create Unparser");
      createUnparserButton.setSelection(true);
      createUnparserButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
      createUnparserButton.addListener(SWT.Selection, new Listener() {
         public void handleEvent(Event event)
         {
            updateStatus();
            if (createUnparserButton.getSelection())
            {
               unparserTechnologyButtons.setEnabled(true);
            } else
            {
               unparserTechnologyButtons.setEnabled(false);
            }
         }
      });
   }

   private void updateStatus()
   {

      if (fileExtension.length() == 0)
      {
         updateStatus("file extension must be given!");
      } else if (!createParser() && !createUnparser())
      {
         updateStatus("Either \"Create Parser\" or \"Create Unparser\" must be checked!");
      } else
      {
         updateStatus(null);
      }
   }

   private void fileExtensionRow(Composite container)
   {
      Label fileExtensionLabel = new Label(container, SWT.NULL);
      fileExtensionLabel.setText("File extension:");
      final Text fileExtensionText = new Text(container, SWT.BORDER | SWT.SINGLE);
      fileExtensionText.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e)
         {
            fileExtension = fileExtensionText.getText();
            updateStatus();
         }
      });
      Label placeHolder = new Label(container, SWT.NULL);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      placeHolder.setLayoutData(gd);
   }

   private final void updateStatus(String message)
   {
      setErrorMessage(message);
      setPageComplete(message == null);
   }

   @Override
   public boolean canFlipToNextPage()
   {
      return false;
   }

   public String getFileExtension()
   {
      return fileExtension;
   }

   public Technology getParserTechnology()
   {
      return parserTechnologyButtons.getTechnology();
   }

   public Technology getUnparserTechnology()
   {
      return unparserTechnologyButtons.getTechnology();
   }

   public boolean createParser()
   {
      return createParserButton.getSelection();
   }

   public boolean createUnparser()
   {
      return createUnparserButton.getSelection();
   }

}
