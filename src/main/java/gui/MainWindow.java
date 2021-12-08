package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.Panel;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Properties;

import javax.swing.JRootPane;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;

public class MainWindow {
    private Menu menu;
    private MenuItem mntmfile;
    private Menu menu_1;
    private MenuItem mntmhelp;
    private Menu menu_2;
    private ToolBar toolBar;
    private Composite log;
    private MenuItem mntmstartListening;
    private MenuItem mntmabout;
    private ToolItem tltmStartListening;
    private ToolItem tltmStopListening;
    private MenuItem mntmStopListening;
    private MenuItem mntmtools;
    private Menu menu_3;
    private MenuItem mntmsettings_1;
    private Group grpLog;
    private static Text txtLog;
    private static SerialPort port;
    private static Properties prop;
    private static String textLog, compareLog;
    private static int logType;
    private Composite statusBar;
    private static Label lblProgramStatus;
    private MenuItem mntmSaveLog;
    private MenuItem mntmexit;
    private MenuItem mntmLoadLog;
    private static Shell shlBspectrum;
    private static boolean unsavedLog;
    private ToolItem tltmGenerateChart;
    @SuppressWarnings("unused")
    private ToolItem toolItem;
    private ToolItem tltmGenerateTable;
    private static Table table;
    private MenuItem mntmview;
    private Menu menu_4;
    private MenuItem mntmlog;
    private SashForm mainForm;
    private static SashForm table_graph_sash;
    private Composite table_graph;
    private MenuItem mntmTable;
    private MenuItem mntmGraph;
    private Composite cmpTable;
    private ToolBar tableToolBar;
    private ToolItem tltmFindMax;
    private ToolItem tltmFindMin;
    private ToolItem tltmClearLog;
    @SuppressWarnings("unused")
    private ToolItem toolItem_1;
    private static ToolItem tltmInterpolate;
    private MenuItem mntmGenerateTable;
    private MenuItem mntmGeneratechart;
    private Composite cmpGraph;
    private ToolBar graphToolBar;
    private ToolItem tltmOpenLog;
    private ToolItem tltmSaveLog;
    @SuppressWarnings("unused")
    private ToolItem toolItem_2;

    private Composite chartContainer;
    private Frame frame;
    private Panel panel;
    private JRootPane rootPane;
    private static ChartPanel chart;
    private Crosshair xCrosshair, yCrosshair;
    private CrosshairOverlay crosshairOverlay;
    private boolean enableCrosshair = false;
    private ToolItem tltmEnableCrosshair;
    private static ToolItem tltmViewPoints;
    private ToolItem tltmInterpolationSettings;

    private static int interStart = 0, interEnd = 100, interStep = 10, eqX = 0, eqY = 0, apprDigits = 4, r, g, b;
    private static boolean showEq = false, interSet = false, showPoints = false;
    private static String lineLabel = "Regression Line";
    private static Paint line, shape;
    private ToolItem tltmChangeColors;
    private MenuItem mntmExport;

    private boolean foundMax = false, foundMin = false;
    private int foundMaxIndex, foundMinIndex;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            MainWindow window = new MainWindow();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        textLog = "";
        compareLog = "";
        final Display display = Display.getDefault();
        shlBspectrum = new Shell();
        shlBspectrum.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("main_icon.ico")));
        shlBspectrum.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent arg0) {
                exitbSpectrum();
            }
        });
        shlBspectrum.setSize(1024, 640);
        shlBspectrum.setText("bSpectrum");
        GridLayout gl_shlBspectrum = new GridLayout(3, false);
        gl_shlBspectrum.marginWidth = 0;
        gl_shlBspectrum.marginHeight = 0;
        shlBspectrum.setLayout(gl_shlBspectrum);
        shlBspectrum.setMaximized(true);

        menu = new Menu(shlBspectrum, SWT.BAR);
        shlBspectrum.setMenuBar(menu);

        mntmfile = new MenuItem(menu, SWT.CASCADE);
        mntmfile.setText("&File");

        menu_1 = new Menu(mntmfile);
        mntmfile.setMenu(menu_1);

        mntmstartListening = new MenuItem(menu_1, SWT.NONE);
        mntmstartListening.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                startListening();
            }
        });
        mntmstartListening.setText("&Start Listening");

        mntmStopListening = new MenuItem(menu_1, SWT.NONE);
        mntmStopListening.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                stopListening();
            }
        });
        mntmStopListening.setText("St&op Listening");

        new MenuItem(menu_1, SWT.SEPARATOR);

        mntmLoadLog = new MenuItem(menu_1, SWT.NONE);
        mntmLoadLog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (!port.isOpened()) {
                    if (txtLog.getText().length() == 0) {
                        FileDialog dlgOpen = new FileDialog(shlBspectrum.getShell(), SWT.OPEN);
                        dlgOpen.setText("Select Log...");
                        String[] filter_ext = { "*.txt" };
                        String[] filter_name = { "Text file (*.txt)" };
                        dlgOpen.setFilterNames(filter_name);
                        dlgOpen.setFilterExtensions(filter_ext);
                        if (dlgOpen.open() != null)
                            try {
                                Path file = Paths.get(new File(dlgOpen.getFilterPath(), dlgOpen.getFileName()).getAbsolutePath());
                                BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF8"));
                                String line = null;
                                while ((line = reader.readLine()) != null)
                                    txtLog.append(line + "\n");
                                reader.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    } else {
                        MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        msg.setMessage("There is currently another log loaded! Do you want to erase it?");
                        msg.setText("bSpectrum");
                        if (msg.open() == SWT.YES)
                            txtLog.setText("");
                        FileDialog dlgOpen = new FileDialog(shlBspectrum.getShell(), SWT.OPEN);
                        dlgOpen.setText("Select Log...");
                        String[] filter_ext = { "*.txt" };
                        String[] filter_name = { "Text file (*.txt)" };
                        dlgOpen.setFilterNames(filter_name);
                        dlgOpen.setFilterExtensions(filter_ext);
                        if (dlgOpen.open() != null)
                            try {
                                Path file = Paths.get(new File(dlgOpen.getFilterPath(), dlgOpen.getFileName()).getAbsolutePath());
                                BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF8"));
                                String line = null;
                                while ((line = reader.readLine()) != null)
                                    txtLog.append(line + "\n");
                                reader.close();
                                foundMax = false;
                                foundMin = false;
                                foundMaxIndex = 0;
                                foundMinIndex = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                } else {
                    MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_WARNING);
                    msg.setMessage("bSpectrum is currently listening! Please stop listening and retry.");
                    msg.setText("Warning");
                    msg.open();
                }
            }
        });
        mntmLoadLog.setText("&Open Log\tCtrl+O");
        mntmLoadLog.setAccelerator(SWT.CTRL + 'O');

        mntmSaveLog = new MenuItem(menu_1, SWT.NONE);
        mntmSaveLog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (txtLog.getText().length() > 0) {
                    FileDialog dlgSave = new FileDialog(shlBspectrum, SWT.SAVE);
                    dlgSave.setText("Save Log...");
                    String[] filter_ext = { "*.txt" };
                    String[] filter_name = { "Text file (*.txt)" };
                    dlgSave.setFilterNames(filter_name);
                    dlgSave.setFilterExtensions(filter_ext);
                    if (dlgSave.open() != null) {
                        Path file = Paths.get(new File(dlgSave.getFilterPath(), dlgSave.getFileName()).getAbsolutePath());
                        try {
                            BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF8"));
                            writer.write(txtLog.getText());
                            writer.close();
                            unsavedLog = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mntmSaveLog.setText("Save &Log\tCtrl+S");
        mntmSaveLog.setAccelerator(SWT.CTRL + 'S');

        new MenuItem(menu_1, SWT.SEPARATOR);

        mntmExport = new MenuItem(menu_1, SWT.NONE);
        mntmExport.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (table.getItemCount() > 0) {
                    FileDialog dlgExport = new FileDialog(shlBspectrum, SWT.SAVE);
                    dlgExport.setText("Export data...");
                    dlgExport.setFilterNames(new String[] { "Microsoft Office Excel 2007 (*.xlsx)" });
                    dlgExport.setFilterExtensions(new String[] { ".xlsx" });
                    if (dlgExport.open() != null) {
                        Workbook wb = new XSSFWorkbook();
                        Sheet sheet = wb.createSheet();
                        for (int i = 0; i < table.getItemCount(); i++) {
                            Row row = sheet.createRow(i);
                            for (int j = 0; j < table.getColumnCount(); j++) {
                                Cell cell = row.createCell(j);
                                cell.setCellValue(Double.parseDouble(table.getItem(i).getText(j)));
                            }
                        }
                        try {
                            FileOutputStream expFile = new FileOutputStream(new File(dlgExport.getFilterPath(), dlgExport.getFileName()).getAbsolutePath());
                            wb.write(expFile);
                            expFile.close();
                            wb.close();
                        } catch (Exception e) {
                            MessageBox msg = new MessageBox(shlBspectrum);
                            msg.setMessage("An error occurred during the export operation!\n" + e.getMessage());
                            msg.setText("bSpectrum");
                            msg.open();
                        }
                    }
                }
            }
        });
        mntmExport.setText("E&xport\tCtrl+E");
        mntmExport.setAccelerator(SWT.CTRL + 'E');

        new MenuItem(menu_1, SWT.SEPARATOR);

        mntmexit = new MenuItem(menu_1, SWT.NONE);
        mntmexit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                exitbSpectrum();
            }
        });
        mntmexit.setText("&Exit\tCtrl+Q");
        mntmexit.setAccelerator(SWT.CTRL + 'Q');

        mntmview = new MenuItem(menu, SWT.CASCADE);
        mntmview.setText("&View");

        menu_4 = new Menu(mntmview);
        mntmview.setMenu(menu_4);

        mntmlog = new MenuItem(menu_4, SWT.CHECK);
        mntmlog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (!mntmlog.getSelection())
                    mainForm.setMaximizedControl(table_graph);
                else
                    mainForm.setMaximizedControl(null);
            }
        });
        mntmlog.setSelection(true);
        mntmlog.setAccelerator(SWT.CTRL + 'L');
        mntmlog.setText("&Log\tCtrl+L");

        new MenuItem(menu_4, SWT.SEPARATOR);

        mntmTable = new MenuItem(menu_4, SWT.CHECK);
        mntmTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (!mntmTable.getSelection())
                    table_graph_sash.setMaximizedControl(cmpGraph);
                else
                    table_graph_sash.setMaximizedControl(null);
            }
        });
        mntmTable.setSelection(true);
        mntmTable.setAccelerator(SWT.CTRL + 'T');
        mntmTable.setText("Table\tCtrl+T");

        mntmGraph = new MenuItem(menu_4, SWT.CHECK);
        mntmGraph.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (!mntmGraph.getSelection())
                    table_graph_sash.setMaximizedControl(cmpTable);
                else
                    table_graph_sash.setMaximizedControl(null);
            }
        });
        mntmGraph.setSelection(true);
        mntmGraph.setAccelerator(SWT.CTRL + 'G');
        mntmGraph.setText("Graph\tCtrl+G");

        mntmtools = new MenuItem(menu, SWT.CASCADE);
        mntmtools.setText("&Tools");

        menu_3 = new Menu(mntmtools);
        mntmtools.setMenu(menu_3);

        mntmsettings_1 = new MenuItem(menu_3, SWT.NONE);
        mntmsettings_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                SettingsWindow settings = new SettingsWindow(shlBspectrum, SWT.DIALOG_TRIM);
                settings.open();
            }
        });
        mntmsettings_1.setText("&Settings");

        new MenuItem(menu_3, SWT.SEPARATOR);

        mntmGenerateTable = new MenuItem(menu_3, SWT.NONE);
        mntmGenerateTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (txtLog.getText().length() > 0)
                    parseLog();
            }
        });
        mntmGenerateTable.setText("Generate T&able");

        mntmGeneratechart = new MenuItem(menu_3, SWT.NONE);
        mntmGeneratechart.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                generateChart();
                table_graph_sash.setWeights(new int[] { 1, 2 });
            }
        });
        mntmGeneratechart.setText("Generate &Chart");

        mntmhelp = new MenuItem(menu, SWT.CASCADE);
        mntmhelp.setText("&Help");

        menu_2 = new Menu(mntmhelp);
        mntmhelp.setMenu(menu_2);

        mntmabout = new MenuItem(menu_2, SWT.NONE);
        mntmabout.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                AboutWindow ab = new AboutWindow(shlBspectrum, SWT.DIALOG_TRIM);
                ab.open();
            }
        });
        mntmabout.setText("&About");

        toolBar = new ToolBar(shlBspectrum, SWT.FLAT | SWT.RIGHT);
        toolBar.setBackgroundImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("toolbar.png")));
        toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

        tltmOpenLog = new ToolItem(toolBar, SWT.NONE);
        tltmOpenLog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (!port.isOpened()) {
                    if (txtLog.getText().length() == 0) {
                        FileDialog dlgOpen = new FileDialog(shlBspectrum.getShell(), SWT.OPEN);
                        dlgOpen.setText("Select Log...");
                        String[] filter_ext = { "*.txt" };
                        String[] filter_name = { "Text file (*.txt)" };
                        dlgOpen.setFilterNames(filter_name);
                        dlgOpen.setFilterExtensions(filter_ext);
                        if (dlgOpen.open() != null)
                            try {
                                Path file = Paths.get(new File(dlgOpen.getFilterPath(), dlgOpen.getFileName()).getAbsolutePath());
                                BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF8"));
                                String line = null;
                                while ((line = reader.readLine()) != null)
                                    txtLog.append(line + "\n");
                                reader.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    } else {
                        MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        msg.setMessage("There is currently another log loaded! Do you want to erase it?");
                        msg.setText("bSpectrum");
                        if (msg.open() == SWT.YES)
                            txtLog.setText("");
                        FileDialog dlgOpen = new FileDialog(shlBspectrum.getShell(), SWT.OPEN);
                        dlgOpen.setText("Select Log...");
                        String[] filter_ext = { "*.txt" };
                        String[] filter_name = { "Text file (*.txt)" };
                        dlgOpen.setFilterNames(filter_name);
                        dlgOpen.setFilterExtensions(filter_ext);
                        if (dlgOpen.open() != null)
                            try {
                                Path file = Paths.get(new File(dlgOpen.getFilterPath(), dlgOpen.getFileName()).getAbsolutePath());
                                BufferedReader reader = Files.newBufferedReader(file, Charset.forName("UTF8"));
                                String line = null;
                                while ((line = reader.readLine()) != null)
                                    txtLog.append(line + "\n");
                                reader.close();
                                foundMax = false;
                                foundMin = false;
                                foundMaxIndex = 0;
                                foundMinIndex = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                } else {
                    MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_WARNING);
                    msg.setMessage("bSpectrum is currently listening! Please stop listening and retry.");
                    msg.setText("Warning");
                    msg.open();
                }
            }
        });
        tltmOpenLog.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("folder_page.png")));
        tltmOpenLog.setText("Open Log");

        tltmSaveLog = new ToolItem(toolBar, SWT.NONE);
        tltmSaveLog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (txtLog.getText().length() > 0) {
                    FileDialog dlgSave = new FileDialog(shlBspectrum.getShell(), SWT.SAVE);
                    dlgSave.setText("Save Log...");
                    String[] filter_ext = { "*.txt" };
                    String[] filter_name = { "Text file (*.txt)" };
                    dlgSave.setFilterNames(filter_name);
                    dlgSave.setFilterExtensions(filter_ext);
                    if (dlgSave.open() != null) {
                        Path file = Paths.get(new File(dlgSave.getFilterPath(), dlgSave.getFileName()).getAbsolutePath());
                        try {
                            BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF8"));
                            writer.write(txtLog.getText());
                            writer.close();
                            unsavedLog = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        tltmSaveLog.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("disk.png")));
        tltmSaveLog.setText("Save Log");

        toolItem_2 = new ToolItem(toolBar, SWT.SEPARATOR);

        tltmStartListening = new ToolItem(toolBar, SWT.NONE);
        tltmStartListening.setToolTipText("Start listening to the port with the current settings");
        tltmStartListening.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                startListening();
            }
        });
        tltmStartListening.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("play.png")));
        tltmStartListening.setText("Start Listening");

        tltmStopListening = new ToolItem(toolBar, SWT.NONE);
        tltmStopListening.setToolTipText("Stop the program from listening to the port");
        tltmStopListening.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                stopListening();
            }
        });
        tltmStopListening.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("stop.png")));
        tltmStopListening.setText("Stop Listening");

        toolItem = new ToolItem(toolBar, SWT.SEPARATOR);

        tltmGenerateTable = new ToolItem(toolBar, SWT.NONE);
        tltmGenerateTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (txtLog.getText().length() > 0)
                    parseLog();
            }
        });
        tltmGenerateTable.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("table.png")));
        tltmGenerateTable.setText("Generate Table");

        tltmGenerateChart = new ToolItem(toolBar, SWT.NONE);
        tltmGenerateChart.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                generateChart();
                table_graph_sash.setWeights(new int[] { 1, 2 });
            }
        });
        tltmGenerateChart.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("chart_bar.png")));
        tltmGenerateChart.setText("Generate Chart");

        toolItem_1 = new ToolItem(toolBar, SWT.SEPARATOR);

        tltmClearLog = new ToolItem(toolBar, SWT.NONE);
        tltmClearLog.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("cancel.png")));
        tltmClearLog.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                txtLog.setText("");
            }
        });
        tltmClearLog.setText("Clear Log");

        mainForm = new SashForm(shlBspectrum, SWT.VERTICAL);
        mainForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

        log = new Composite(mainForm, SWT.NONE);
        log.setLayout(new GridLayout(1, false));

        grpLog = new Group(log, SWT.NONE);
        grpLog.setLayout(new FillLayout(SWT.HORIZONTAL));
        GridData gd_grpLog = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_grpLog.heightHint = 128;
        grpLog.setLayoutData(gd_grpLog);
        grpLog.setText("Log");

        txtLog = new Text(grpLog, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);

        table_graph = new Composite(mainForm, SWT.NONE);
        table_graph.setLayout(new GridLayout(1, false));

        table_graph_sash = new SashForm(table_graph, SWT.NONE);
        table_graph_sash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        cmpTable = new Composite(table_graph_sash, SWT.NONE);
        GridLayout gl_cmpTable = new GridLayout(1, false);
        gl_cmpTable.verticalSpacing = 0;
        cmpTable.setLayout(gl_cmpTable);

        tableToolBar = new ToolBar(cmpTable, SWT.FLAT | SWT.RIGHT);
        tableToolBar.setBackgroundImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("toolbar.png")));
        tableToolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        tltmFindMax = new ToolItem(tableToolBar, SWT.NONE);
        tltmFindMax.setText("Find Max");
        tltmFindMax.setToolTipText("Find the maximum absorbance value");
        tltmFindMax.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("max.png")));
        tltmFindMax.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (table.getItemCount() > 0) {
                    if (foundMax)
                        table.getItem(foundMaxIndex).setBackground(display.getSystemColor(SWT.COLOR_WHITE));
                    if (logType == 1) {
                        RangeWindow r = new RangeWindow(shlBspectrum, SWT.DIALOG_TRIM);
                        r.setMin(Integer.parseInt(table.getItem(0).getText(1)));
                        r.setMax(Integer.parseInt(table.getItem(table.getItemCount() - 1).getText(1)));
                        r.open();
                        int index = 0;
                        for (int i = 0; i < table.getItemCount(); i++)
                            if (r.getStart() > Integer.parseInt(table.getItem(index).getText(1)))
                                index++;
                        double max = Double.parseDouble(table.getItem(index).getText(2).split(" ")[0]);
                        for (int i = index; i <= r.getEnd() - r.getStart(); i++)
                            if (Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]) > max) {
                                max = Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]);
                                foundMaxIndex = i;
                            }
                    } else {
                        double max = Double.parseDouble(table.getItem(0).getText(2).split(" ")[0]);
                        for (int i = 0; i < table.getItemCount(); i++)
                            if (Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]) > max) {
                                max = Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]);
                                foundMaxIndex = i;
                            }
                    }
                    table.getItem(foundMaxIndex).setBackground(display.getSystemColor(SWT.COLOR_GREEN));
                    foundMax = true;
                }
            }
        });

        tltmFindMin = new ToolItem(tableToolBar, SWT.NONE);
        tltmFindMin.setText("Find Min");
        tltmFindMin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (table.getItemCount() > 0) {
                    if (foundMin)
                        table.getItem(foundMinIndex).setBackground(display.getSystemColor(SWT.COLOR_WHITE));
                    if (logType == 1) {
                        RangeWindow r = new RangeWindow(shlBspectrum, SWT.DIALOG_TRIM);
                        r.setMin(Integer.parseInt(table.getItem(0).getText(1)));
                        r.setMax(Integer.parseInt(table.getItem(table.getItemCount() - 1).getText(1)));
                        r.open();
                        int index = 0;
                        for (int i = 0; i < table.getItemCount(); i++)
                            if (r.getStart() > Integer.parseInt(table.getItem(index).getText(1)))
                                index++;
                        double min = Double.parseDouble(table.getItem(index).getText(2).split(" ")[0]);
                        for (int i = index; i <= r.getEnd() - r.getStart(); i++)
                            if (Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]) < min) {
                                min = Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]);
                                foundMinIndex = i;
                            }
                    } else {
                        double min = Double.parseDouble(table.getItem(0).getText(2).split(" ")[0]);
                        for (int i = 0; i < table.getItemCount(); i++)
                            if (Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]) < min) {
                                min = Double.parseDouble(table.getItem(i).getText(2).split(" ")[0]);
                                foundMinIndex = i;
                            }
                    }
                    table.getItem(foundMinIndex).setBackground(display.getSystemColor(SWT.COLOR_RED));
                    foundMin = true;
                }
            }
        });
        tltmFindMin.setToolTipText("Find the minimum absorbance value");
        tltmFindMin.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("min.png")));

        table = new Table(cmpTable, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        table.setSize(498, 471);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        cmpGraph = new Composite(table_graph_sash, SWT.NONE);
        cmpGraph.setLayout(new GridLayout(1, false));

        graphToolBar = new ToolBar(cmpGraph, SWT.FLAT | SWT.RIGHT);
        graphToolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        graphToolBar.setBackgroundImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("toolbar.png")));

        tltmChangeColors = new ToolItem(graphToolBar, SWT.NONE);
        tltmChangeColors.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (chart.isVisible()) {
                    XYPlot plot = chart.getChart().getXYPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    ColorDialog dlg = new ColorDialog(shlBspectrum, SWT.DIALOG_TRIM);
                    dlg.setLine(renderer.getSeriesPaint(0));
                    dlg.setShape(renderer.getSeriesFillPaint(0));
                    dlg.open();
                    line = dlg.getLine();
                    shape = dlg.getShape();
                    generateChart();
                }
            }
        });
        tltmChangeColors.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("palette.png")));
        tltmChangeColors.setText("Change Colors");

        tltmEnableCrosshair = new ToolItem(graphToolBar, SWT.CHECK);
        tltmEnableCrosshair.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("crosshair.png")));
        tltmEnableCrosshair.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (chart.isVisible())
                    enableCrosshair = tltmEnableCrosshair.getSelection();
                else
                    tltmEnableCrosshair.setSelection(false);
            }
        });
        tltmEnableCrosshair.setText("Enable Crosshair");

        tltmViewPoints = new ToolItem(graphToolBar, SWT.CHECK);
        tltmViewPoints.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (chart.isVisible()) {
                    generateChart();
                } else
                    tltmViewPoints.setSelection(false);
            }
        });
        tltmViewPoints.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("point.png")));
        tltmViewPoints.setText("View Points");

        tltmInterpolate = new ToolItem(graphToolBar, SWT.CHECK);
        tltmInterpolate.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("interpolate.png")));
        tltmInterpolate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (chart.isVisible()) {
                    if (logType == 0) {
                        generateChart();
                    } else {
                        tltmInterpolate.setSelection(false);
                        MessageBox msg = new MessageBox(shlBspectrum, 0);
                        msg.setMessage("It makes no sense trying to interpolate this chart!\nThe absorbance at different wavelengths can be obtained only via empirical ways!");
                        msg.setText("bSpectrum");
                        msg.open();
                    }
                } else
                    tltmInterpolate.setSelection(false);
            }
        });
        tltmInterpolate.setText("Interpolate");

        tltmInterpolationSettings = new ToolItem(graphToolBar, SWT.NONE);
        tltmInterpolationSettings.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (chart.isVisible() && logType == 0) {
                    InterpolationWindow i = new InterpolationWindow(shlBspectrum, SWT.DIALOG_TRIM, interStart, interEnd, interStep, showEq, showPoints, eqX, eqY, apprDigits, lineLabel, r, g, b);
                    i.open();
                    interStart = i.getInterStart();
                    interEnd = i.getInterEnd();
                    interStep = i.getInterStep();
                    showEq = i.getShowEq();
                    showPoints = i.getShowPoints();
                    eqX = i.getEqX();
                    eqY = i.getEqY();
                    apprDigits = i.getApproximation();
                    lineLabel = i.getLineLabel();
                    r = i.getR();
                    g = i.getG();
                    b = i.getB();
                    interSet = true;
                    generateChart();
                }
            }
        });
        tltmInterpolationSettings.setText("Interpolation Settings");
        tltmInterpolationSettings.setImage(new Image(null, MainWindow.class.getClassLoader().getResourceAsStream("settings.png")));

        chartContainer = new Composite(cmpGraph, SWT.EMBEDDED);
        chartContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
        chartContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        frame = SWT_AWT.new_Frame(chartContainer);

        panel = new Panel();
        frame.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        rootPane = new JRootPane();
        panel.add(rootPane);

        chart = new ChartPanel((JFreeChart) null);
        chart.setVisible(false);
        chart.setMouseWheelEnabled(true);
        this.crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, java.awt.Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
        this.yCrosshair = new Crosshair(Double.NaN, java.awt.Color.GRAY, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chart.addOverlay(crosshairOverlay);
        chart.addChartMouseListener(new ChartMouseListener() {
            public void chartMouseClicked(ChartMouseEvent arg0) {
                // Ignore this
            }

            public void chartMouseMoved(ChartMouseEvent event) {
                if (enableCrosshair) {
                    Rectangle2D dataArea = chart.getScreenDataArea();
                    JFreeChart chart = event.getChart();
                    XYPlot plot = (XYPlot) chart.getPlot();
                    ValueAxis xAxis = plot.getDomainAxis();
                    if (logType == 1) {
                        int x = (int) xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, (double) x);
                        xCrosshair.setValue(x);
                        yCrosshair.setValue(y);
                    } else {
                        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, (double) x);
                        xCrosshair.setValue(x);
                        yCrosshair.setValue(y);
                    }
                }
            }
        });
        rootPane.getContentPane().add(chart, BorderLayout.CENTER);
        table_graph_sash.setWeights(new int[] { 1, 1 });
        mainForm.setWeights(new int[] { 163, 481 });

        statusBar = new Composite(shlBspectrum, SWT.BORDER);
        statusBar.setLayout(new GridLayout(1, false));
        statusBar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 3, 1));

        lblProgramStatus = new Label(statusBar, SWT.BORDER | SWT.SHADOW_IN);
        lblProgramStatus.setText("Paused");
        lblProgramStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        shlBspectrum.open();
        shlBspectrum.layout();

        prop = new Properties();
        try {
            File f = new File(System.getProperty("user.dir") + "/config.xml");
            if (f.exists()) {
                prop.loadFromXML(new FileInputStream(System.getProperty("user.dir") + "/config.xml"));
                port = new SerialPort(prop.getProperty("LastUsedPort"));
            } else {
                MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_WARNING);
                msg.setMessage("Missing configuration file! Using default settings...\nPlease check settings for selecting the correct parameters!");
                msg.setText("Error!");
                msg.open();
                prop.setProperty("LastUsedPort", "COM1");
                prop.setProperty("BaudRate", "9600");
                prop.setProperty("DataBits", "8");
                prop.setProperty("Parity", "None");
                prop.setProperty("StopBits", "1");
                prop.setProperty("LogUpdateTime", "200");
                prop.storeToXML(new FileOutputStream(System.getProperty("user.dir") + "/config.xml"), null);
                port = new SerialPort(prop.getProperty("LastUsedPort"));
            }
        } catch (Exception e) {
            MessageBox msg = new MessageBox(shlBspectrum);
            msg.setMessage("An error occurred during the creation of config.xml!\n" + e.getMessage());
            msg.setText("bSpectrum");
            msg.open();
        }

        Runnable timer = new Runnable() {
            int time = Integer.parseInt(prop.getProperty("LogUpdateTime"));

            @Override
            public void run() {
                if (port != null && port.isOpened() && textLog != compareLog) {
                    txtLog.setText(textLog);
                    txtLog.setTopIndex(txtLog.getLineCount() - 1);
                }
                if (textLog != compareLog)
                    compareLog = textLog;
                display.timerExec(time, this);
            }
        };
        int time = Integer.parseInt(prop.getProperty("LogUpdateTime"));
        display.timerExec(time, timer);
        while (!shlBspectrum.isDisposed())
            if (!display.readAndDispatch())
                display.sleep();
    }

    public static void startListening() {
        if (port == null)
            port = new SerialPort(prop.getProperty("LastUsedPort"));
        if (port.isOpened() == false)
            try {
                port.openPort();
                if (port.isOpened()) {
                    port.setParams(Integer.parseInt(prop.getProperty("BaudRate")), Integer.parseInt(prop.getProperty("DataBits")), Integer.parseInt(prop.getProperty("StopBits")),
                                   getParity(prop.getProperty("Parity")));
                    port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    port.addEventListener(new SerialPortReader(), SerialPort.MASK_RXCHAR);
                    lblProgramStatus.setText("Listening...");
                } else
                    throw new Exception();
            } catch (Exception e) {
                MessageBox msg = new MessageBox(shlBspectrum);
                msg.setMessage("An error occurred during the port listening operation!\n" + e.getMessage());
                msg.setText("bSpectrum");
                msg.open();
            }
    }

    public static void stopListening() {
        try {
            if (port != null && port.isOpened()) {
                port.closePort();
                lblProgramStatus.setText("Paused");
            }
        } catch (Exception e) {
            MessageBox msg = new MessageBox(shlBspectrum);
            msg.setMessage("An error occurred during the port listening operation!\n" + e.getMessage());
            msg.setText("bSpectrum");
            msg.open();
        }
    }

    private static void parseLog() {
        if (table.getItemCount() > 0) {
            table.removeAll();
            if (table.getColumnCount() > 0)
                while (table.getColumnCount() > 0)
                    table.getColumns()[0].dispose();
        }
        try {
            if (txtLog.getText().contains("Lambda Report")) {
                logType = 0;
                TableColumn number = new TableColumn(table, SWT.NONE), wavelength = new TableColumn(table, SWT.NONE), absorbance = new TableColumn(table, SWT.NONE);
                number.setText("#");
                wavelength.setText("Wavelength");
                absorbance.setText("Absorbance");
                wavelength.setResizable(true);
                absorbance.setResizable(true);
                number.setWidth(30);
                wavelength.setWidth(100);
                absorbance.setWidth(100);
                for (int i = 0; i < txtLog.getLineCount(); i++) {
                    TableItem item = new TableItem(table, SWT.NONE);
                    String sampleNum = "", wave, absorb;
                    while (getLine(txtLog.getText(), i).contains("Sample #") == false && i < txtLog.getLineCount())
                        i++;
                    if (getLine(txtLog.getText(), i).length() > 0 && getLine(txtLog.getText(), i).contains("Sample #"))
                        sampleNum = "" + Integer.parseInt(getLine(txtLog.getText(), i).substring(8).trim());
                    while (getLine(txtLog.getText(), i).contains(" nm") == false && i < txtLog.getLineCount())
                        i++;
                    if (getLine(txtLog.getText(), i).length() > 0 && getLine(txtLog.getText(), i).contains(" nm")) {
                        wave = "" + Integer.parseInt(getLine(txtLog.getText(), i).substring(3, 6));
                        absorb = "" + Double.parseDouble(getLine(txtLog.getText(), i).substring(15, 20));
                        item.setText(new String[] { sampleNum, wave, absorb });
                        item.setBackground(0, new Color(Display.getCurrent(), 138, 207, 249));
                    } else
                        item.dispose();
                }
            } else if (txtLog.getText().toLowerCase().contains("scan date")) {
                logType = 1;
                TableColumn number = new TableColumn(table, SWT.NONE), wavelength = new TableColumn(table, SWT.NONE), absorbance = new TableColumn(table, SWT.NONE);
                number.setText("#");
                wavelength.setText("Wavelength");
                absorbance.setText("Absorbance");
                wavelength.setResizable(true);
                absorbance.setResizable(true);
                number.setWidth(30);
                wavelength.setWidth(100);
                absorbance.setWidth(100);
                int i = 0;
                while (getLine(txtLog.getText().toLowerCase(), i).contains("scan date") == false)
                    i++;
                for (i += 2; i < txtLog.getLineCount() - 11; i++) {
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(new String[] { "" + (i - 1), "" + Integer.parseInt(getLine(txtLog.getText(), i).split(":")[0].trim()),
                            "" + Double.parseDouble(getLine(txtLog.getText(), i).split(":")[1].trim()) });
                    item.setBackground(0, new Color(Display.getCurrent(), 138, 207, 249));
                }
            } else {
                MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_INFORMATION);
                msg.setMessage("Unknown log type! bSpectrum cannot parse this type of log!");
                msg.setText("bSpectrum");
                msg.open();
            }
        } catch (Exception e) {
            MessageBox msg = new MessageBox(shlBspectrum, SWT.ICON_ERROR);
            msg.setMessage("An error occurred during the parsing process!\n" + e.getMessage());
            msg.setText("bSpectrum");
            msg.open();
        }
    }

    private static void generateChart() {
        try {
            if (table.getItemCount() > 0) {
                if (logType == 0) {
                    XYSeriesCollection chart_data = new XYSeriesCollection();
                    XYSeries xy = new XYSeries("Absorbance");
                    if (table.getColumnCount() == 3) {
                        ConcentrationDialog dlg = new ConcentrationDialog(shlBspectrum, SWT.DIALOG_TRIM, table.getItemCount());
                        dlg.open();
                        TableColumn conc = new TableColumn(table, SWT.NONE);
                        conc.setText("Concentration");
                        conc.setResizable(true);
                        conc.setWidth(100);
                        for (int i = 0; i < table.getItemCount(); i++) {
                            xy.add(dlg.getConcentrations()[i], Double.parseDouble(table.getItem(i).getText(2)));
                            table.getItem(i).setText(new String[] { table.getItem(i).getText(0), table.getItem(i).getText(1), table.getItem(i).getText(2), "" + dlg.getConcentrations()[i] });
                        }
                    } else {
                        for (int i = 0; i < table.getItemCount(); i++)
                            xy.add(Double.parseDouble(table.getItem(i).getText(3)), Double.parseDouble(table.getItem(i).getText(2)));
                    }
                    chart_data.addSeries(xy);
                    JFreeChart lineChart = ChartFactory.createXYLineChart("Multiple samples absorbance", "Concentration (nmol)", "Absorbance", chart_data, PlotOrientation.VERTICAL, true, true, false);
                    chart.setChart(lineChart);
                    XYPlot plot = lineChart.getXYPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, line);

                    if (tltmViewPoints.getSelection()) {
                        renderer.setSeriesFillPaint(0, shape);
                        renderer.setUseFillPaint(true);
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShapesFilled(0, true);
                    }

                    if (tltmInterpolate.getSelection()) {
                        if (interSet == false) {
                            InterpolationWindow i = new InterpolationWindow(shlBspectrum, SWT.DIALOG_TRIM, interStart, interEnd, interStep, showEq, showPoints, eqX, eqY, apprDigits, lineLabel, r, g,
                                    b);
                            i.open();
                            interStart = i.getInterStart();
                            interEnd = i.getInterEnd();
                            interStep = i.getInterStep();
                            showEq = i.getShowEq();
                            showPoints = i.getShowPoints();
                            eqX = i.getEqX();
                            eqY = i.getEqY();
                            apprDigits = i.getApproximation();
                            lineLabel = i.getLineLabel();
                            r = i.getR();
                            g = i.getG();
                            b = i.getB();
                            interSet = true;
                        }
                        double regression_parameter[] = Regression.getOLSRegression(chart_data, 0);
                        LineFunction2D line = new LineFunction2D(regression_parameter[0], regression_parameter[1]);
                        XYDataset data = DatasetUtilities.sampleFunction2D(line, interStart, interEnd, interStep, lineLabel);

                        plot.setDataset(1, data);
                        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, showPoints);
                        renderer1.setSeriesPaint(0, new java.awt.Color(r, g, b));
                        plot.setRenderer(1, renderer1);

                        if (showEq) {
                            XYTextAnnotation equation;
                            if (regression_parameter[0] < 0.0D)
                                equation = new XYTextAnnotation("y=" + decimalDigits(apprDigits, regression_parameter[1]) + "x" + decimalDigits(apprDigits, regression_parameter[0]), eqX, eqY);
                            else
                                equation = new XYTextAnnotation("y=" + decimalDigits(apprDigits, regression_parameter[1]) + "x+" + decimalDigits(apprDigits, regression_parameter[0]), eqX, eqY);
                            plot.addAnnotation(equation);
                        }
                    }
                } else if (logType == 1) {
                    XYSeriesCollection chart_data = new XYSeriesCollection();
                    XYSeries xy = new XYSeries("Absorbance");
                    for (int i = 0; i < table.getItemCount(); i++)
                        xy.add(Integer.parseInt(table.getItem(i).getText(1)), Double.parseDouble(table.getItem(i).getText(2)));
                    chart_data.addSeries(xy);
                    JFreeChart lineChart = ChartFactory.createXYLineChart("Absorbance chart", "Wavelength (nm)", "Absorbance", chart_data, PlotOrientation.VERTICAL, true, true, false);
                    chart.setChart(lineChart);
                    XYPlot plot = lineChart.getXYPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, line);

                    if (tltmViewPoints.getSelection()) {
                        renderer.setUseFillPaint(true);
                        renderer.setSeriesFillPaint(0, shape);
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShapesFilled(0, true);
                    }
                }
                chart.setVisible(true);
            }
        } catch (Exception e) {
            MessageBox msg = new MessageBox(shlBspectrum);
            msg.setMessage("An error occurred during the creation of the graph!\n" + e.getMessage());
            msg.setText("bSpectrum");
            msg.open();
        }
    }

    private static void exitbSpectrum() {
        if (unsavedLog) {
            MessageBox msg = new MessageBox(shlBspectrum, SWT.YES | SWT.NO);
            msg.setMessage("You have an unsaved log. Do you want to save it?");
            msg.setText("bSpectrum");
            if (msg.open() == SWT.YES) {
                FileDialog dlgSave = new FileDialog(shlBspectrum.getShell(), SWT.SAVE);
                dlgSave.setText("Save Log...");
                String[] filter_ext = { "*.txt" };
                String[] filter_name = { "Text file (*.txt)" };
                dlgSave.setFilterNames(filter_name);
                dlgSave.setFilterExtensions(filter_ext);
                if (dlgSave.open() != null) {
                    Path file = Paths.get(new File(dlgSave.getFilterPath(), dlgSave.getFileName()).getAbsolutePath());
                    try {
                        BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF8"));
                        writer.write(txtLog.getText());
                        writer.close();
                        unsavedLog = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (port != null && port.isOpened())
            try {
                port.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        shlBspectrum.dispose();
        System.exit(0);
    }

    private static int getParity(String p) {
        switch (p) {
        case "None":
            return 0;
        case "Odd":
            return 1;
        case "Even":
            return 2;
        default:
            return 0;
        }
    }

    private static String getLine(String str, int n) {
        String[] lines = str.split("\r\n|\r|\n");
        if (n < lines.length)
            return lines[n];
        else
            return "";
    }

    private static class SerialPortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0)
                try {
                    textLog += port.readString(event.getEventValue());
                    unsavedLog = true;
                } catch (SerialPortException ex) {
                    MessageBox msg = new MessageBox(shlBspectrum);
                    msg.setMessage("An error occurred during the port listening operation!\n" + ex.getMessage());
                    msg.setText("bSpectrum");
                    msg.open();
                }
        }
    }

    public static String decimalDigits(int decimaldigits, double x) {
        final NumberFormat numFormat = NumberFormat.getNumberInstance();
        numFormat.setMaximumFractionDigits(decimaldigits);
        final String resultS = numFormat.format(x);
        return resultS;
    }
}
