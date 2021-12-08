package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ConcentrationDialog extends Dialog {

    protected Object result;
    protected Shell shlConcentration;
    private Table table;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnOk;
    private Text text;
    private double concentrations[];
    private TableColumn tblclmnConcentrationValues;
    private int min;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ConcentrationDialog(Shell parent, int style, int min) {
        super(parent, style);
        setText("SWT Dialog");
        this.min = min;
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shlConcentration.open();
        shlConcentration.layout();
        Display display = getParent().getDisplay();
        while (!shlConcentration.isDisposed()) {
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
        shlConcentration = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.ON_TOP | SWT.APPLICATION_MODAL);
        shlConcentration.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent arg0) {
                if (table.getItemCount() >= min) {
                    arg0.doit = true;
                } else {
                    MessageBox msg = new MessageBox(shlConcentration, SWT.ICON_ERROR);
                    msg.setMessage("Please type at least " + min + " values!");
                    msg.setText("bSpectrum");
                    msg.open();
                    arg0.doit = false;
                }
            }
        });
        shlConcentration.setSize(452, 372);
        shlConcentration.setText("Concentrations");
        shlConcentration.setLayout(new GridLayout(3, true));
        shlConcentration.setLocation((getParent().getDisplay().getPrimaryMonitor().getBounds().x + (getParent().getDisplay().getPrimaryMonitor().getBounds().width - shlConcentration.getBounds().width) / 2),
                                     (getParent().getDisplay().getPrimaryMonitor().getBounds().y + (getParent().getDisplay().getPrimaryMonitor().getBounds().height - shlConcentration.getBounds().height) / 2));

        table = new Table(shlConcentration, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        table.setLinesVisible(true);

        tblclmnConcentrationValues = new TableColumn(table, SWT.NONE);
        tblclmnConcentrationValues.setResizable(false);
        tblclmnConcentrationValues.setWidth(432);
        tblclmnConcentrationValues.setText("Concentration values");

        text = new Text(shlConcentration, SWT.BORDER);
        text.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent arg0) {
                if (arg0.detail == SWT.TRAVERSE_RETURN) {
                    if (text.getText().length() > 0) {
                        try {
                            double num = Double.parseDouble(text.getText());
                            TableItem i = new TableItem(table, 0);
                            i.setText("" + num);
                            text.setText("");
                        } catch (Exception e) {
                            MessageBox msg = new MessageBox(shlConcentration, SWT.ICON_ERROR);
                            msg.setMessage("An error occurred!\n" + e.getMessage());
                            msg.setText("bSpectrum");
                            msg.open();
                        }
                    } else {
                        if (table.getItemCount() >= min) {
                            concentrations = new double[table.getItemCount()];
                            for (int i = 0; i < table.getItemCount(); i++)
                                concentrations[i] = Double.parseDouble(table.getItem(i).getText(0));
                            shlConcentration.close();
                        } else {
                            MessageBox msg = new MessageBox(shlConcentration, SWT.ICON_ERROR);
                            msg.setMessage("Please type at least " + min + " values!");
                            msg.setText("bSpectrum");
                            msg.open();
                        }
                    }
                }
            }
        });
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnAdd = new Button(shlConcentration, SWT.NONE);
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    double num = Double.parseDouble(text.getText());
                    TableItem i = new TableItem(table, 0);
                    i.setText("" + num);
                    text.setText("");
                } catch (Exception e) {
                    MessageBox msg = new MessageBox(shlConcentration, SWT.ICON_ERROR);
                    msg.setMessage("An error occurred!\n" + e.getMessage());
                    msg.setText("bSpectrum");
                    msg.open();
                }
            }
        });
        btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnAdd.setText("Add");

        btnDelete = new Button(shlConcentration, SWT.NONE);
        btnDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (table.getSelectionIndex() > -1)
                    table.remove(table.getSelectionIndex());
            }
        });
        btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnDelete.setText("Delete");

        btnOk = new Button(shlConcentration, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (table.getItemCount() >= min) {
                    concentrations = new double[table.getItemCount()];
                    for (int i = 0; i < table.getItemCount(); i++)
                        concentrations[i] = Double.parseDouble(table.getItem(i).getText(0));
                    shlConcentration.close();
                } else {
                    MessageBox msg = new MessageBox(shlConcentration, SWT.ICON_ERROR);
                    msg.setMessage("Please type at least " + min + " values!");
                    msg.setText("bSpectrum");
                    msg.open();
                }
            }
        });
        btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        btnOk.setText("OK");

    }

    public double[] getConcentrations() {
        return concentrations;
    }

    public void setConcentrations(double concentrations[]) {
        this.concentrations = concentrations;
    }
}
