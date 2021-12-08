package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class RangeWindow extends Dialog {

    protected Object result;
    protected Shell shlBspectrum;
    private Label lblSearchStartWavelenght;
    private Spinner spnStart;
    private Label lblSearchEndWavelenght;
    private Spinner spnEnd;
    private Button btnOk;

    private int start, end;
    private int max, min;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public RangeWindow(Shell parent, int style) {
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
        shlBspectrum.open();
        shlBspectrum.layout();
        Display display = getParent().getDisplay();
        while (!shlBspectrum.isDisposed()) {
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
        shlBspectrum = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
        shlBspectrum.setSize(249, 118);
        shlBspectrum.setText("bSpectrum");
        shlBspectrum.setLayout(new GridLayout(2, false));
        shlBspectrum.setLocation((getParent().getDisplay().getPrimaryMonitor().getBounds().x + (getParent().getDisplay().getPrimaryMonitor().getBounds().width - shlBspectrum.getBounds().width) / 2),
                                 (getParent().getDisplay().getPrimaryMonitor().getBounds().y + (getParent().getDisplay().getPrimaryMonitor().getBounds().height - shlBspectrum.getBounds().height) / 2));

        lblSearchStartWavelenght = new Label(shlBspectrum, SWT.NONE);
        lblSearchStartWavelenght.setText("Search Start Wavelenght:");

        spnStart = new Spinner(shlBspectrum, SWT.BORDER);
        spnStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        spnStart.setMaximum(max);
        spnStart.setMinimum(min);
        spnStart.setSelection(min);

        lblSearchEndWavelenght = new Label(shlBspectrum, SWT.NONE);
        lblSearchEndWavelenght.setText("Search End Wavelenght:");

        spnEnd = new Spinner(shlBspectrum, SWT.BORDER);
        spnEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        spnEnd.setMaximum(max);
        spnEnd.setMinimum(min);
        spnEnd.setSelection(max);

        btnOk = new Button(shlBspectrum, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (start <= end) {
                    start = spnStart.getSelection();
                    end = spnEnd.getSelection();
                    shlBspectrum.close();
                } else {
                    MessageBox msg = new MessageBox(shlBspectrum, 0);
                    msg.setMessage("Invalid values detected!");
                    msg.setText("bSpectrum");
                    msg.open();
                }
            }
        });
        btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        btnOk.setText("OK");

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setMax(int smax) {
        this.max = smax;
    }

    public void setMin(int smin) {
        this.min = smin;
    }
}
