package gui;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class AboutWindow extends Dialog {

    protected Object result;
    protected Shell shlAboutBspectrum;
    private Label lblImage;
    private Composite composite;
    private Label lblBspectrum;
    private Label lblAProgramDesigned;
    private Label lblCopyright;
    private Text txtThisProgramIs;
    private Button btnSeeLicense;
    private Composite composite_1;
    private Button btnOk;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public AboutWindow(Shell parent, int style) {
        super(parent, style);
        setText("SWT Dialog");
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shlAboutBspectrum.open();
        shlAboutBspectrum.layout();
        Display display = getParent().getDisplay();
        while (!shlAboutBspectrum.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shlAboutBspectrum = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shlAboutBspectrum.setSize(580, 500);
        shlAboutBspectrum.setText("About bSpectrum");
        shlAboutBspectrum.setLayout(new GridLayout(2, false));
        shlAboutBspectrum.setLocation((getParent().getDisplay().getPrimaryMonitor().getBounds().x + (getParent().getDisplay().getPrimaryMonitor().getBounds().width - shlAboutBspectrum.getBounds().width) / 2),
                                      (getParent().getDisplay().getPrimaryMonitor().getBounds().y + (getParent().getDisplay().getPrimaryMonitor().getBounds().height - shlAboutBspectrum.getBounds().height) / 2));

        lblImage = new Label(shlAboutBspectrum, SWT.NONE);
        lblImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
//        lblImage.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("about.png");
        lblImage.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("about.png")));

        composite = new Composite(shlAboutBspectrum, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        lblBspectrum = new Label(composite, SWT.NONE);
        lblBspectrum.setFont(SWTResourceManager.getFont("Calibri", 14, SWT.BOLD));
        lblBspectrum.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        lblBspectrum.setAlignment(SWT.CENTER);
        lblBspectrum.setText("bSpectrum");

        lblAProgramDesigned = new Label(composite, SWT.NONE);
        lblAProgramDesigned.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
        lblAProgramDesigned.setAlignment(SWT.CENTER);
        lblAProgramDesigned.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblAProgramDesigned.setText("A program designed to work with BioRad\u00AE\u008D SmartSpec\u00AE 3000 spectrophotometer");

        lblCopyright = new Label(composite, SWT.NONE);
        lblCopyright.setAlignment(SWT.CENTER);
        lblCopyright.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblCopyright.setText("Copyright\u00A9 2015 - Matteo Iervasi");

        txtThisProgramIs = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
        txtThisProgramIs.setEditable(false);
        txtThisProgramIs.setText("This program is free software: you can redistribute it and/or modify\r\nit under the terms of the GNU General Public License as published by\r\nthe Free Software Foundation, either version 3 of the License, or\r\n(at your option) any later version.\r\n\r\nThis program is distributed in the hope that it will be useful,\r\nbut WITHOUT ANY WARRANTY; without even the implied warranty of\r\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\r\nGNU General Public License for more details.\r\n\r\nYou should have received a copy of the GNU General Public License\r\nalong with this program.  If not, see <http://www.gnu.org/licenses/>.");
        txtThisProgramIs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        composite_1 = new Composite(composite, SWT.NONE);
        composite_1.setLayout(new GridLayout(1, false));
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        btnSeeLicense = new Button(composite_1, SWT.NONE);
        btnSeeLicense.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    File f = new File("LICENSE.txt");
                    if (f.exists())
                        java.awt.Desktop.getDesktop().edit(new File("LICENSE.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        GridData gd_btnSeeLicense = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
        gd_btnSeeLicense.widthHint = 100;
        btnSeeLicense.setLayoutData(gd_btnSeeLicense);
        btnSeeLicense.setText("See License");

        btnOk = new Button(composite_1, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                shlAboutBspectrum.close();
            }
        });
        GridData gd_btnOk = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_btnOk.widthHint = 100;
        btnOk.setLayoutData(gd_btnOk);
        btnOk.setText("OK");

    }

}
