package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import jssc.SerialPortList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class SettingsWindow extends Dialog {

    protected Object result;
    protected Shell shlSettings;
    private Group grpPortOptions;
    private Label lblUsePort;
    private Combo cmbPortNames;
    private Label lblBaudRate;
    private Combo cmbBaudRate;
    private Label lblDataBits;
    private Label lblParity;
    private Label lblStopBits;
    private Combo cmbDataBits;
    private Combo cmbParity;
    private Combo cmbStopBits;
    private Button btnOk;
    private Button btnCancel;
    private Composite composite;
    private Properties prop;
    private Group grpMiscellanous;
    private Label lblLogUpdateTime;
    private Spinner spinner;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public SettingsWindow(Shell parent, int style) {
        super(parent, style);
        setText("Settings");
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shlSettings.open();
        shlSettings.layout();
        Display display = getParent().getDisplay();
        while (!shlSettings.isDisposed()) {
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
        shlSettings = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shlSettings.addShellListener(new ShellAdapter() {
            public void shellActivated(ShellEvent arg0) {
                try {
                    String[] portNames = SerialPortList.getPortNames();
                    for (int i = 0; i < portNames.length; i++) {
                        cmbPortNames.add(portNames[i]);
                    }
                    prop = new Properties();
                    if (new File(System.getProperty("user.dir") + "/config.xml").exists()) {
                        prop.loadFromXML(new FileInputStream(System.getProperty("user.dir") + "/config.xml"));
                        cmbPortNames.select(cmbPortNames.indexOf(prop.getProperty("LastUsedPort")));
                        cmbBaudRate.select(cmbBaudRate.indexOf(prop.getProperty("BaudRate")));
                        cmbDataBits.select(cmbDataBits.indexOf(prop.getProperty("DataBits")));
                        cmbParity.select(cmbParity.indexOf(prop.getProperty("Parity")));
                        cmbStopBits.select(cmbStopBits.indexOf(prop.getProperty("StopBits")));
                        spinner.setSelection(Integer.parseInt(prop.getProperty("LogUpdateTime")));
                    }
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        });
        shlSettings.setSize(410, 380);
        shlSettings.setText("Settings");
        shlSettings.setLayout(new GridLayout(2, false));
        shlSettings.setLocation((getParent().getDisplay().getPrimaryMonitor().getBounds().x + (getParent().getDisplay().getPrimaryMonitor().getBounds().width - shlSettings.getBounds().width) / 2),
                                (getParent().getDisplay().getPrimaryMonitor().getBounds().y + (getParent().getDisplay().getPrimaryMonitor().getBounds().height - shlSettings.getBounds().height) / 2));

        grpPortOptions = new Group(shlSettings, SWT.NONE);
        grpPortOptions.setLayout(new GridLayout(2, false));
        grpPortOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        grpPortOptions.setText("Port Options");

        lblUsePort = new Label(grpPortOptions, SWT.NONE);
        lblUsePort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblUsePort.setText("Use Port:");

        cmbPortNames = new Combo(grpPortOptions, SWT.READ_ONLY);
        cmbPortNames.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        lblBaudRate = new Label(grpPortOptions, SWT.NONE);
        lblBaudRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblBaudRate.setText("Baud Rate:");

        cmbBaudRate = new Combo(grpPortOptions, SWT.READ_ONLY);
        cmbBaudRate.setItems(new String[] { "110", "300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "28800", "38400", "56000", "57600", "115200" });
        cmbBaudRate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        lblDataBits = new Label(grpPortOptions, SWT.NONE);
        lblDataBits.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDataBits.setText("Data Bits:");

        cmbDataBits = new Combo(grpPortOptions, SWT.READ_ONLY);
        cmbDataBits.setItems(new String[] { "5", "6", "7", "8" });
        cmbDataBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        lblParity = new Label(grpPortOptions, SWT.NONE);
        lblParity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblParity.setText("Parity:");

        cmbParity = new Combo(grpPortOptions, SWT.READ_ONLY);
        cmbParity.setItems(new String[] { "Odd", "Even", "None", "Mark", "Space" });
        cmbParity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        lblStopBits = new Label(grpPortOptions, SWT.NONE);
        lblStopBits.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblStopBits.setText("Stop Bits:");

        cmbStopBits = new Combo(grpPortOptions, SWT.READ_ONLY);
        cmbStopBits.setItems(new String[] { "1", "2" });
        cmbStopBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        grpMiscellanous = new Group(shlSettings, SWT.NONE);
        grpMiscellanous.setLayout(new GridLayout(2, false));
        grpMiscellanous.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        grpMiscellanous.setText("Miscellanous");

        lblLogUpdateTime = new Label(grpMiscellanous, SWT.NONE);
        lblLogUpdateTime.setText("Log update time (ms):");

        spinner = new Spinner(grpMiscellanous, SWT.BORDER);
        spinner.setMaximum(5000);
        spinner.setSelection(200);

        composite = new Composite(shlSettings, SWT.NONE);
        composite.setLayout(new GridLayout(2, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

        btnOk = new Button(composite, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    prop.setProperty("LastUsedPort", cmbPortNames.getText());
                    prop.setProperty("BaudRate", cmbBaudRate.getText());
                    prop.setProperty("DataBits", cmbDataBits.getText());
                    prop.setProperty("Parity", cmbParity.getText());
                    prop.setProperty("StopBits", cmbStopBits.getText());
                    prop.setProperty("LogUpdateTime", "" + spinner.getSelection());
                    prop.storeToXML(new FileOutputStream(System.getProperty("user.dir") + "/config.xml"), null);
                    shlSettings.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnOk.setText("OK");

        btnCancel = new Button(composite, SWT.NONE);
        btnCancel.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                shlSettings.close();
            }
        });
        btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnCancel.setText("Cancel");

    }

}
