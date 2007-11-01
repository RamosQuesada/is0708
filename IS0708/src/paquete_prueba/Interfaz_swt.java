package paquete_prueba;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class Interfaz_swt {

  public static void main(String[] IS0708) {
    final Display display = new Display();
    final Shell shell = new Shell();

    shell.setText("Título de la ventana");
    shell.setVisible(true);

    GridLayout layout = new GridLayout();
    layout.numColumns = 4;

    shell.setLayout(layout);

    final Text texto = new Text(shell, SWT.BORDER);
    texto.setEditable(true);
    texto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));

    final Button boton = new Button(shell, SWT.PUSH);
    boton.setText("Mostrar en la esquina");
    boton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));

    SelectionAdapter sel = new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        final ToolTip hssTip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_WORKING);
        hssTip.setMessage(texto.getText());

        Tray hssTray = display.getSystemTray();

        if (hssTray != null) {
          TrayItem hssItem = new TrayItem(hssTray, SWT.NONE);
          hssTip.setText("Mensaje");
          hssItem.setToolTip(hssTip);
          hssTip.setVisible(true);
        }
      }
    };

    boton.addSelectionListener(sel);

    shell.pack();
    shell.setSize(400, 100);
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    display.dispose();
  }
}