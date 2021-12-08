package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class InterpolationWindow extends Dialog {

    protected Object result;
    protected Shell shlInterpolate;
    private Label lblInterpolationStartPoint;
    private Label lblInterpolationEndPoint;
    private Spinner spnStart;
    private Spinner spnEnd;
    private Label lblInterpolationStep;
    private Spinner spnStep;
    private Button btnShowEquation;
    private Group grpEquation;
    private Label lblLabelCoordinates;
    private Spinner spnEqX;
    private Spinner spnEqY;
    private Button btnOk;
    private Label lblRegressionLineLabel;
    private Text txtLineLabel;
    private Label lblApproximate;
    private Spinner spnDigits;
    private Composite composite;
    private String lineLabel;
    private Composite composite_1;
    private Canvas canvas;
    private Button btnChangeRegressionLine;

    private int interStart, interEnd, interStep, eqX, eqY, apprDigits, r, g, b;
    private boolean showEq, showPoints;
    private Button btnShowPoints;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public InterpolationWindow(Shell parent, int style, int start, int end, int step, boolean eq, boolean points, int eqx, int eqy, int apprDigits, String lineLabel, int r, int g, int b) {
        super(parent, style);
        setText("SWT Dialog");
        this.interStart = start;
        this.interEnd = end;
        this.interStep = step;
        this.eqX = eqx;
        this.eqY = eqy;
        this.showEq = eq;
        this.apprDigits = apprDigits;
        this.lineLabel = lineLabel;
        this.r = r;
        this.g = g;
        this.b = b;
        this.showPoints = points;
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shlInterpolate.open();
        shlInterpolate.layout();
        Display display = getParent().getDisplay();
        while (!shlInterpolate.isDisposed()) {
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
        shlInterpolate = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.ON_TOP | SWT.APPLICATION_MODAL);
        shlInterpolate.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent arg0) {
                interStart = spnStart.getSelection();
                interEnd = spnEnd.getSelection();
                interStep = spnStep.getSelection();
                eqX = spnEqX.getSelection();
                eqY = spnEqY.getSelection();
                showEq = btnShowEquation.getSelection();
                lineLabel = txtLineLabel.getText();
                apprDigits = spnDigits.getSelection();
                showPoints = btnShowPoints.getSelection();
                arg0.doit = true;
            }
        });
        shlInterpolate.setSize(318, 331);
        shlInterpolate.setText("Interpolation Settings");
        shlInterpolate.setLayout(new GridLayout(2, false));
        shlInterpolate.setLocation((getParent().getDisplay().getPrimaryMonitor().getBounds().x + (getParent().getDisplay().getPrimaryMonitor().getBounds().width - shlInterpolate.getBounds().width) / 2),
                                   (getParent().getDisplay().getPrimaryMonitor().getBounds().y + (getParent().getDisplay().getPrimaryMonitor().getBounds().height - shlInterpolate.getBounds().height) / 2));

        lblInterpolationStartPoint = new Label(shlInterpolate, SWT.NONE);
        lblInterpolationStartPoint.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblInterpolationStartPoint.setText("Interpolation Start Point:");

        spnStart = new Spinner(shlInterpolate, SWT.BORDER);
        spnStart.setMaximum(1000);
        spnStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        lblInterpolationEndPoint = new Label(shlInterpolate, SWT.NONE);
        lblInterpolationEndPoint.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblInterpolationEndPoint.setText("Interpolation End Point:");

        spnEnd = new Spinner(shlInterpolate, SWT.BORDER);
        spnEnd.setMaximum(1000);
        spnEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        lblInterpolationStep = new Label(shlInterpolate, SWT.NONE);
        lblInterpolationStep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblInterpolationStep.setText("Interpolation Step:");

        spnStep = new Spinner(shlInterpolate, SWT.BORDER);
        spnStep.setMaximum(1000);
        spnStep.setSelection(10);
        spnStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        grpEquation = new Group(shlInterpolate, SWT.NONE);
        grpEquation.setLayout(new GridLayout(3, false));
        GridData gd_grpEquation = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_grpEquation.heightHint = 70;
        grpEquation.setLayoutData(gd_grpEquation);
        grpEquation.setText("Equation");

        btnShowEquation = new Button(grpEquation, SWT.CHECK);
        btnShowEquation.setSelection(true);
        btnShowEquation.setText("Show Equation");

        composite = new Composite(grpEquation, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        composite.setLayout(new GridLayout(2, false));

        lblApproximate = new Label(composite, SWT.NONE);
        lblApproximate.setSize(115, 15);
        lblApproximate.setText("Approximation Digits:");

        spnDigits = new Spinner(composite, SWT.BORDER);
        spnDigits.setMaximum(18);
        spnDigits.setSize(47, 22);

        lblLabelCoordinates = new Label(grpEquation, SWT.NONE);
        lblLabelCoordinates.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblLabelCoordinates.setText("Label Coordinates:");

        spnEqX = new Spinner(grpEquation, SWT.BORDER);
        spnEqX.setMaximum(1000);
        spnEqX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        spnEqX.setMinimum(-1000);
        this.spnEqX.setSelection(eqX);

        spnEqY = new Spinner(grpEquation, SWT.BORDER);
        spnEqY.setMaximum(1000);
        spnEqY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        spnEqY.setMinimum(-1000);
        this.spnEqY.setSelection(eqY);

        lblRegressionLineLabel = new Label(shlInterpolate, SWT.NONE);
        lblRegressionLineLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblRegressionLineLabel.setText("Regression Line Label:");

        txtLineLabel = new Text(shlInterpolate, SWT.BORDER);
        txtLineLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(shlInterpolate, SWT.NONE);

        btnShowPoints = new Button(shlInterpolate, SWT.CHECK);
        btnShowPoints.setText("Show Points");
        this.btnShowPoints.setSelection(showPoints);

        composite_1 = new Composite(shlInterpolate, SWT.NONE);
        composite_1.setLayout(new GridLayout(2, false));
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

        canvas = new Canvas(composite_1, SWT.NONE);
        GridData gd_canvas = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_canvas.widthHint = 30;
        gd_canvas.heightHint = 30;
        canvas.setLayoutData(gd_canvas);

        btnChangeRegressionLine = new Button(composite_1, SWT.NONE);
        btnChangeRegressionLine.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                ColorDialog color = new ColorDialog(shlInterpolate);
                RGB rgb = new RGB(r, g, b);
                color.setRGB(rgb);
                color.open();
                r = color.getRGB().red;
                g = color.getRGB().green;
                b = color.getRGB().blue;
                canvas.setBackground(new Color(Display.getCurrent(), r, g, b));
            }
        });
        btnChangeRegressionLine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnChangeRegressionLine.setText("Change Regression Line Color");

        btnOk = new Button(shlInterpolate, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                interStart = spnStart.getSelection();
                interEnd = spnEnd.getSelection();
                interStep = spnStep.getSelection();
                eqX = spnEqX.getSelection();
                eqY = spnEqY.getSelection();
                showEq = btnShowEquation.getSelection();
                lineLabel = txtLineLabel.getText();
                apprDigits = spnDigits.getSelection();
                showPoints = btnShowPoints.getSelection();
                shlInterpolate.close();
            }
        });
        btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        btnOk.setText("OK");
        spnStart.setMinimum(-1000);

        this.spnStart.setSelection(interStart);
        spnEnd.setMinimum(-1000);
        this.spnEnd.setSelection(interEnd);
        this.spnStep.setSelection(interStep);
        this.btnShowEquation.setSelection(showEq);
        this.txtLineLabel.setText(lineLabel);
        this.spnDigits.setSelection(apprDigits);
        this.canvas.setBackground(new Color(Display.getCurrent(), r, g, b));

    }

    public int getInterStart() {
        return interStart;
    }

    public int getInterEnd() {
        return interEnd;
    }

    public int getInterStep() {
        return interStep;
    }

    public int getEqX() {
        return eqX;
    }

    public int getEqY() {
        return eqY;
    }

    public boolean getShowEq() {
        return showEq;
    }

    public boolean getShowPoints() {
        return showPoints;
    }

    public int getApproximation() {
        return apprDigits;
    }

    public String getLineLabel() {
        return lineLabel;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
