package gui;

import java.awt.Color;
import java.awt.Paint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class ColorDialog extends Dialog {

    protected Object result;
    protected Shell shlChangeColors;
    private Button btnLine;
    private Canvas cnvLine;
    private Button btnOk;

    private Paint line, shape;
    private Button btnShapes;
    private Canvas cnvShape;

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public ColorDialog(Shell parent, int style) {
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
        shlChangeColors.open();
        shlChangeColors.layout();
        Display display = getParent().getDisplay();
        while (!shlChangeColors.isDisposed()) {
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
        shlChangeColors = new Shell(getParent(), getStyle() | SWT.ON_TOP | SWT.APPLICATION_MODAL);
        shlChangeColors.setMinimumSize(new Point(132, 27));
        shlChangeColors.setSize(236, 138);
        shlChangeColors.setText("Change Colors");
        shlChangeColors.setLayout(new GridLayout(2, true));
        shlChangeColors.setLocation((getParent().getDisplay().getPrimaryMonitor().getBounds().x + (getParent().getDisplay().getPrimaryMonitor().getBounds().width - shlChangeColors.getBounds().width) / 2),
                                    (getParent().getDisplay().getPrimaryMonitor().getBounds().y + (getParent().getDisplay().getPrimaryMonitor().getBounds().height - shlChangeColors.getBounds().height) / 2));

        btnLine = new Button(shlChangeColors, SWT.NONE);
        btnLine.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                org.eclipse.swt.widgets.ColorDialog dlg = new org.eclipse.swt.widgets.ColorDialog(shlChangeColors, 0);
                dlg.setRGB(new RGB(((Color) line).getRed(), ((Color) line).getGreen(), ((Color) line).getBlue()));
                dlg.open();
                line = new Color(dlg.getRGB().red, dlg.getRGB().green, dlg.getRGB().blue);
                cnvLine.setBackground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), ((Color) line).getRed(), ((Color) line).getGreen(), ((Color) line).getBlue()));
            }
        });
        btnLine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnLine.setText("Line");

        btnShapes = new Button(shlChangeColors, SWT.NONE);
        btnShapes.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                org.eclipse.swt.widgets.ColorDialog dlg = new org.eclipse.swt.widgets.ColorDialog(shlChangeColors, 0);
                if (shape != null)
                    dlg.setRGB(new RGB(((Color) shape).getRed(), ((Color) shape).getGreen(), ((Color) shape).getBlue()));
                dlg.open();
                shape = new Color(dlg.getRGB().red, dlg.getRGB().green, dlg.getRGB().blue);
                cnvShape.setBackground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), ((Color) shape).getRed(), ((Color) shape).getGreen(), ((Color) shape).getBlue()));
            }
        });
        btnShapes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnShapes.setText("Shapes");

        cnvLine = new Canvas(shlChangeColors, SWT.NONE);
        GridData gd_cnvLine = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_cnvLine.heightHint = 40;
        gd_cnvLine.widthHint = 40;
        cnvLine.setLayoutData(gd_cnvLine);

        cnvShape = new Canvas(shlChangeColors, SWT.NONE);
        GridData gd_cnvShape = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_cnvShape.widthHint = 40;
        gd_cnvShape.heightHint = 40;
        cnvShape.setLayoutData(gd_cnvShape);
        cnvShape.setBackground(SWTResourceManager.getColor(0, 0, 0));

        btnOk = new Button(shlChangeColors, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                shlChangeColors.close();
            }
        });
        btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        btnOk.setText("OK");

        cnvLine.setBackground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), ((Color) line).getRed(), ((Color) line).getGreen(), ((Color) line).getBlue()));
        if (shape != null)
            cnvShape.setBackground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), ((Color) shape).getRed(), ((Color) shape).getGreen(), ((Color) shape).getBlue()));
    }

    public Paint getLine() {
        return line;
    }

    public void setLine(Paint line) {
        this.line = line;
    }

    public Paint getShape() {
        return shape;
    }

    public void setShape(Paint shape) {
        this.shape = shape;
    }
}
