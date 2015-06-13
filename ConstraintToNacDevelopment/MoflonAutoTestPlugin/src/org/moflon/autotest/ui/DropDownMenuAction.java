package org.moflon.autotest.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.moflon.autotest.core.AutoTestController;
import org.moflon.autotest.core.DeploymentController;

public class DropDownMenuAction implements IWorkbenchWindowPulldownDelegate
{
   private static final String DEPLOY = "Deploy eMoflon";

   private static final String CONFIGURE = "Install and Configure eMoflon";

   private static final String DEPLOY_AS_BETA = "Beta (restricted access)";

   private static final String DEPLOY_AS_RELEASE = "Release (restricted access)";

   private static final String DEPLOY_LOCAL = "Local";

   private static final String DEV_WORKSPACE = "Developer Workspace";

   private static final String TEST_WORKSPACE_MISC = "Test Workspace: Misc.";

   private static final String TEST_WORKSPACE_TGG_0 = "TGG Test Workspace: TGG[0]";

   private static final String TEST_WORKSPACE_TGG_1 = "TGG Test Workspace: TGG[1]";
   
   private static final String TRANSFORMATION_ZOO_0 = "Transformation Zoo [0]";
   
   private static final String TRANSFORMATION_ZOO_1 = "Transformation Zoo [1] (restricted acces)";
   
   private static final String TEST_WORKSPACE_DEMOCLES_0 = "Democles [0]";
   
   private static final String TEST_TEXTUAL = "Run all Tests for Textual Syntax";
   
   private Menu mainMenu;

   private AutoTestController workspaceController;

   private DeploymentController deploymentController;
   
   @Override
   public void init(IWorkbenchWindow window)
   {
      workspaceController = new AutoTestController(window);
      deploymentController = new DeploymentController(window);
   }

   @Override
   public Menu getMenu(Control parent)
   {
      mainMenu = new Menu(parent);

      createAndAddInstallAndConfigureMenuItem();

      createSeparator(mainMenu);

      createAndAddDeployMenuItem();
            
      return mainMenu;
   }


   private void createSeparator(Menu menu)
   {
      new MenuItem(menu, SWT.SEPARATOR);
   }

   private void createAndAddInstallAndConfigureMenuItem()
   {
      MenuItem configure = createAndAddMenuItem(CONFIGURE, mainMenu, SWT.CASCADE);

      Menu configureOptions = new Menu(mainMenu);
      configure.setMenu(configureOptions);

      createAndAddMenuItem(DEV_WORKSPACE, configureOptions);

      createSeparator(configureOptions);

      createAndAddMenuItem(TEST_WORKSPACE_MISC, configureOptions);
      createAndAddMenuItem(TEST_WORKSPACE_TGG_0, configureOptions);
      createAndAddMenuItem(TEST_WORKSPACE_TGG_1, configureOptions);
      
      createSeparator(configureOptions);
      
      createAndAddMenuItem(TEST_WORKSPACE_DEMOCLES_0, configureOptions);
      
      createSeparator(configureOptions);
      
      createAndAddMenuItem(TRANSFORMATION_ZOO_0, configureOptions);
      createAndAddMenuItem(TRANSFORMATION_ZOO_1, configureOptions);
      
      createSeparator(configureOptions);
      
      createAndAddMenuItem(TEST_TEXTUAL, configureOptions);
   }

   private void createAndAddDeployMenuItem()
   {
      MenuItem deploy = createAndAddMenuItem(DEPLOY, mainMenu, SWT.CASCADE);

      Menu deploymentOptions = new Menu(mainMenu);
      deploy.setMenu(deploymentOptions);

      createAndAddMenuItem(DEPLOY_LOCAL, deploymentOptions);

      createSeparator(deploymentOptions);

      createAndAddMenuItem(DEPLOY_AS_BETA, deploymentOptions);
      createAndAddMenuItem(DEPLOY_AS_RELEASE, deploymentOptions);
   }
   
   private void createAndAddMenuItem(String nameOfMenuItem, Menu parent)
   {
      createAndAddMenuItem(nameOfMenuItem, parent, SWT.NONE);
   }

   private MenuItem createAndAddMenuItem(final String nameOfMenuItem, Menu parent, int style)
   {
      MenuItem menuItem = new MenuItem(parent, style);
      menuItem.setText(nameOfMenuItem);

      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            Display.getDefault().asyncExec(new Runnable() {
               @Override
               public void run()
               {
                  runForchosenAction(nameOfMenuItem);
               }
            });
         }
      });

      return menuItem;
   }

   public void runForchosenAction(String nameOfMenuItem)
   {
      switch (nameOfMenuItem)
      {
      case DEPLOY_LOCAL:
         deploymentController.deployLocal();
         break;

      case DEPLOY_AS_BETA:
         deploymentController.deployBeta();
         break;

      case DEPLOY_AS_RELEASE:
         deploymentController.deployRelease();
         break;

      case DEV_WORKSPACE:
         workspaceController.installDevWorkspace(DEV_WORKSPACE);
         break;

      case TEST_WORKSPACE_MISC:
         workspaceController.installTestWorkspaceMisc(TEST_WORKSPACE_MISC);
         break;

      case TEST_WORKSPACE_TGG_0:
         workspaceController.installTestWorkspaceTGG_0(TEST_WORKSPACE_TGG_0);
         break;

      case TEST_WORKSPACE_TGG_1:
         workspaceController.installTestWorkspaceTGG_1(TEST_WORKSPACE_TGG_1);
         break;

      case TRANSFORMATION_ZOO_0:
         workspaceController.installTransformationZoo_0(TRANSFORMATION_ZOO_0);
         break;
         
      case TRANSFORMATION_ZOO_1:
         workspaceController.installTransformationZoo_1(TRANSFORMATION_ZOO_1);
         break;
         
      case TEST_WORKSPACE_DEMOCLES_0:
         workspaceController.installTestWorkspaceDemocles_0(TEST_WORKSPACE_DEMOCLES_0);
         break;
         
      case TEST_TEXTUAL:
    	  workspaceController.installTestWorkspaceTextual(TEST_TEXTUAL);
    	  break;
    	  
      default:
         break;
      }
   }

   @Override
   public void run(IAction action)
   {
   }

   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {
   }

   @Override
   public void dispose()
   {
   }
}