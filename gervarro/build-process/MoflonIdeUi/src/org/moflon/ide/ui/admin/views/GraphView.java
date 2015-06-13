package org.moflon.ide.ui.admin.views;

import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.gef4.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.gef4.zest.core.viewers.GraphViewer;
import org.eclipse.gef4.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.gef4.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.gef4.zest.layouts.LayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.moflon.ide.ui.admin.views.util.ModelContentProvider;
import org.moflon.ide.ui.admin.views.util.ModelDropListener;
import org.moflon.ide.ui.admin.views.util.ModelLabelProvider;

public class GraphView extends ViewPart implements IZoomableWorkbenchPart
{

   public static final String ID = "org.moflon.ide.ui.admin.views.GraphView";

   private GraphViewer viewer;

   private ModelDropListener modelDropListener;

   public static GraphView INSTANCE;

   public GraphView()
   {
      super();
      INSTANCE = this;
   }

   public void createPartControl(Composite parent)
   {
      viewer = new GraphViewer(parent, SWT.BORDER);
      viewer.setContentProvider(new ModelContentProvider());
      viewer.setLabelProvider(new ModelLabelProvider());
      LayoutAlgorithm layout = getLayout();
      viewer.setLayoutAlgorithm(layout, true);
      addDropSupport();
      viewer.applyLayout();
      fillToolBar();
   }

   private void addDropSupport()
   {
      int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
      Transfer[] transferTypes = new Transfer[] { LocalTransfer.getInstance(), FileTransfer.getInstance() };

      modelDropListener = new ModelDropListener(viewer);
      viewer.addDropSupport(operations, transferTypes, modelDropListener);
   }

   private LayoutAlgorithm getLayout()
   {
      LayoutAlgorithm layout;
      layout = new TreeLayoutAlgorithm();
      return layout;
   }

   /**
    * Passing the focus request to the viewer's control.
    */

   public void setFocus()
   {
   }

   private void fillToolBar()
   {
      ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
      IActionBars bars = getViewSite().getActionBars();
      bars.getMenuManager().add(toolbarZoomContributionViewItem);
      addLayoutOptions(bars.getMenuManager());
   }

   private void addLayoutOptions(IMenuManager menu)
   {
      menu.add(new Action("Clear View", IAction.AS_PUSH_BUTTON) {
         public void run()
         {
            modelDropListener.clear();
         }
      });

      menu.add(new Action("Redraw Graph", IAction.AS_PUSH_BUTTON) {
         public void run()
         {
            updateViewer();
         }
      });

      menu.add(new Separator());

      Action action = new Action("Tree Layout", IAction.AS_RADIO_BUTTON) {
         public void run()
         {
            viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(), true);
         }
      };
      action.setChecked(true);

      menu.add(action);

      menu.add(new Action("Spring Layout", IAction.AS_RADIO_BUTTON) {
         public void run()
         {
            viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(), true);
         }
      });

      menu.add(new Action("Grid Layout", IAction.AS_RADIO_BUTTON) {
         public void run()
         {
            viewer.setLayoutAlgorithm(new GridLayoutAlgorithm(), true);
         }
      });

      menu.add(new Action("Radial Layout", IAction.AS_RADIO_BUTTON) {
         public void run()
         {
            viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(), true);
         }
      });
   }

   @Override
   public AbstractZoomableViewer getZoomableViewer()
   {
      return viewer;
   }

   public void updateViewer()
   {
      viewer.setInput(viewer.getInput());
   }

}
