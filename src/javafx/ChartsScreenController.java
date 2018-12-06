package javafx;

import bluetooth.BluetoothConnection;
import bluetooth.IPostAppendScreen;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uncoupledglovedatathings.GloveSensors;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChartsScreenController implements Initializable, IPostAppendScreen {


    public VBox accGraphsVbox;
    public VBox gyroGraphsVbox;
    Thread updateThread;

    private Chart2D chartGyroX = new Chart2D();
    private Chart2D chartGyroY = new Chart2D();
    private Chart2D chartGyroZ = new Chart2D();

    private Chart2D chartAccX = new Chart2D();
    private Chart2D chartAccY = new Chart2D();
    private Chart2D chartAccZ = new Chart2D();


    private ITrace2D TraceGyroX1 = new Trace2DLtd(500);
    private ITrace2D TraceGyroX2 = new Trace2DLtd(500);
    private ITrace2D TraceGyroX3 = new Trace2DLtd(500);
    private ITrace2D TraceGyroX4 = new Trace2DLtd(500);
    private ITrace2D TraceGyroX5 = new Trace2DLtd(500);
    private ITrace2D TraceGyroX6 = new Trace2DLtd(500);

    private ITrace2D TraceGyroY1 = new Trace2DLtd(500);
    private ITrace2D TraceGyroY2 = new Trace2DLtd(500);
    private ITrace2D TraceGyroY3 = new Trace2DLtd(500);
    private ITrace2D TraceGyroY4 = new Trace2DLtd(500);
    private ITrace2D TraceGyroY5 = new Trace2DLtd(500);
    private ITrace2D TraceGyroY6 = new Trace2DLtd(500);

    private ITrace2D TraceGyroZ1 = new Trace2DLtd(500);
    private ITrace2D TraceGyroZ2 = new Trace2DLtd(500);
    private ITrace2D TraceGyroZ3 = new Trace2DLtd(500);
    private ITrace2D TraceGyroZ4 = new Trace2DLtd(500);
    private ITrace2D TraceGyroZ5 = new Trace2DLtd(500);
    private ITrace2D TraceGyroZ6 = new Trace2DLtd(500);

    private ITrace2D TraceAccX1 = new Trace2DLtd(500);
    private ITrace2D TraceAccX2 = new Trace2DLtd(500);
    private ITrace2D TraceAccX3 = new Trace2DLtd(500);
    private ITrace2D TraceAccX4 = new Trace2DLtd(500);
    private ITrace2D TraceAccX5 = new Trace2DLtd(500);
    private ITrace2D TraceAccX6 = new Trace2DLtd(500);

    private ITrace2D TraceAccY1 = new Trace2DLtd(500);
    private ITrace2D TraceAccY2 = new Trace2DLtd(500);
    private ITrace2D TraceAccY3 = new Trace2DLtd(500);
    private ITrace2D TraceAccY4 = new Trace2DLtd(500);
    private ITrace2D TraceAccY5 = new Trace2DLtd(500);
    private ITrace2D TraceAccY6 = new Trace2DLtd(500);

    private ITrace2D TraceAccZ1 = new Trace2DLtd(500);
    private ITrace2D TraceAccZ2 = new Trace2DLtd(500);
    private ITrace2D TraceAccZ3 = new Trace2DLtd(500);
    private ITrace2D TraceAccZ4 = new Trace2DLtd(500);
    private ITrace2D TraceAccZ5 = new Trace2DLtd(500);
    private ITrace2D TraceAccZ6 = new Trace2DLtd(500);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setupColors();
        setupTraces();




        SwingNode snChartGyroX = new SwingNode();
        SwingNode snChartGyroY = new SwingNode();
        SwingNode snChartGyroZ = new SwingNode();
        SwingNode snChartAccX = new SwingNode();
        SwingNode snChartAccY = new SwingNode();
        SwingNode snChartAccZ = new SwingNode();

        createSwingContent(snChartGyroX, chartGyroX);
        createSwingContent(snChartGyroY, chartGyroY);
        createSwingContent(snChartGyroZ, chartGyroZ);

        createSwingContent(snChartAccX, chartAccX);
        createSwingContent(snChartAccY, chartAccY);
        createSwingContent(snChartAccZ, chartAccZ);

        gyroGraphsVbox.getChildren().addAll(snChartGyroX,snChartGyroY,snChartGyroZ);
        accGraphsVbox.getChildren().addAll(snChartAccX,snChartAccY,snChartAccZ);


        BluetoothConnection.schedulePostAppendRunnable(this);

    }



    private void createSwingContent(final SwingNode swingNode, JComponent f) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VBox.setVgrow(swingNode, Priority.ALWAYS);
                swingNode.setContent(f);
                f.setBounds(500, 500, 500, 500);
            }
        });
    }

    public void start(ActionEvent event) {
        updateThread.start();

//        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
//        if (btStatus.isConnected()) {
//            virtualHandContolFunction.start();
//            status.setText("Running");
//        } else {
//            showBluetoothDisconnectedAlert();
//        }
    }

    public void stop(ActionEvent event) {
        updateThread.interrupt();
//        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
//        if (btStatus.isConnected()) {
//            virtualHandContolFunction.stop();
//            status.setText("Not Running");
//        } else {
//            showBluetoothDisconnectedAlert();
//        }

    }

    private void showBluetoothDisconnectedAlert() {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.WARNING,
                "You should to connect with the glove first."
        );
        closeConfirmation.setHeaderText("Bluetooth Disconnected");
        closeConfirmation.setTitle("Bluetooth Disconnected");
        closeConfirmation.show();
    }

    @Override
    public Runnable getPostAppendRunnable() {
        return () -> Platform.runLater(() -> {
            TraceGyroX1.addPoint(GloveSensors.getInstance().getSensor1().getGx().getRealSize(), GloveSensors.getInstance().getSensor1().getGx().lastElement());
            TraceGyroX2.addPoint(GloveSensors.getInstance().getSensor2().getGx().getRealSize(), GloveSensors.getInstance().getSensor2().getGx().lastElement());
            TraceGyroX3.addPoint(GloveSensors.getInstance().getSensor3().getGx().getRealSize(), GloveSensors.getInstance().getSensor3().getGx().lastElement());
            TraceGyroX4.addPoint(GloveSensors.getInstance().getSensor4().getGx().getRealSize(), GloveSensors.getInstance().getSensor4().getGx().lastElement());
            TraceGyroX5.addPoint(GloveSensors.getInstance().getSensor5().getGx().getRealSize(), GloveSensors.getInstance().getSensor5().getGx().lastElement());
            TraceGyroX6.addPoint(GloveSensors.getInstance().getSensor6().getGx().getRealSize(), GloveSensors.getInstance().getSensor6().getGx().lastElement());

            TraceGyroY1.addPoint(GloveSensors.getInstance().getSensor1().getGy().getRealSize(), GloveSensors.getInstance().getSensor1().getGy().lastElement());
            TraceGyroY2.addPoint(GloveSensors.getInstance().getSensor2().getGy().getRealSize(), GloveSensors.getInstance().getSensor2().getGy().lastElement());
            TraceGyroY3.addPoint(GloveSensors.getInstance().getSensor3().getGy().getRealSize(), GloveSensors.getInstance().getSensor3().getGy().lastElement());
            TraceGyroY4.addPoint(GloveSensors.getInstance().getSensor4().getGy().getRealSize(), GloveSensors.getInstance().getSensor4().getGy().lastElement());
            TraceGyroY5.addPoint(GloveSensors.getInstance().getSensor5().getGy().getRealSize(), GloveSensors.getInstance().getSensor5().getGy().lastElement());
            TraceGyroY6.addPoint(GloveSensors.getInstance().getSensor6().getGy().getRealSize(), GloveSensors.getInstance().getSensor6().getGy().lastElement());

            TraceGyroZ1.addPoint(GloveSensors.getInstance().getSensor1().getGz().getRealSize(), GloveSensors.getInstance().getSensor1().getGz().lastElement());
            TraceGyroZ2.addPoint(GloveSensors.getInstance().getSensor2().getGz().getRealSize(), GloveSensors.getInstance().getSensor2().getGz().lastElement());
            TraceGyroZ3.addPoint(GloveSensors.getInstance().getSensor3().getGz().getRealSize(), GloveSensors.getInstance().getSensor3().getGz().lastElement());
            TraceGyroZ4.addPoint(GloveSensors.getInstance().getSensor4().getGz().getRealSize(), GloveSensors.getInstance().getSensor4().getGz().lastElement());
            TraceGyroZ5.addPoint(GloveSensors.getInstance().getSensor5().getGz().getRealSize(), GloveSensors.getInstance().getSensor5().getGz().lastElement());
            TraceGyroZ6.addPoint(GloveSensors.getInstance().getSensor6().getGz().getRealSize(), GloveSensors.getInstance().getSensor6().getGz().lastElement());


            TraceAccX1.addPoint(GloveSensors.getInstance().getSensor1().getAx().getRealSize(), GloveSensors.getInstance().getSensor1().getAx().lastElement());
            TraceAccX2.addPoint(GloveSensors.getInstance().getSensor2().getAx().getRealSize(), GloveSensors.getInstance().getSensor2().getAx().lastElement());
            TraceAccX3.addPoint(GloveSensors.getInstance().getSensor3().getAx().getRealSize(), GloveSensors.getInstance().getSensor3().getAx().lastElement());
            TraceAccX4.addPoint(GloveSensors.getInstance().getSensor4().getAx().getRealSize(), GloveSensors.getInstance().getSensor4().getAx().lastElement());
            TraceAccX5.addPoint(GloveSensors.getInstance().getSensor5().getAx().getRealSize(), GloveSensors.getInstance().getSensor5().getAx().lastElement());
            TraceAccX6.addPoint(GloveSensors.getInstance().getSensor6().getAx().getRealSize(), GloveSensors.getInstance().getSensor6().getAx().lastElement());

            TraceAccY1.addPoint(GloveSensors.getInstance().getSensor1().getAy().getRealSize(), GloveSensors.getInstance().getSensor1().getAy().lastElement());
            TraceAccY2.addPoint(GloveSensors.getInstance().getSensor2().getAy().getRealSize(), GloveSensors.getInstance().getSensor2().getAy().lastElement());
            TraceAccY3.addPoint(GloveSensors.getInstance().getSensor3().getAy().getRealSize(), GloveSensors.getInstance().getSensor3().getAy().lastElement());
            TraceAccY4.addPoint(GloveSensors.getInstance().getSensor4().getAy().getRealSize(), GloveSensors.getInstance().getSensor4().getAy().lastElement());
            TraceAccY5.addPoint(GloveSensors.getInstance().getSensor5().getAy().getRealSize(), GloveSensors.getInstance().getSensor5().getAy().lastElement());
            TraceAccY6.addPoint(GloveSensors.getInstance().getSensor6().getAy().getRealSize(), GloveSensors.getInstance().getSensor6().getAy().lastElement());

            TraceAccZ1.addPoint(GloveSensors.getInstance().getSensor1().getAz().getRealSize(), GloveSensors.getInstance().getSensor1().getAz().lastElement());
            TraceAccZ2.addPoint(GloveSensors.getInstance().getSensor2().getAz().getRealSize(), GloveSensors.getInstance().getSensor2().getAz().lastElement());
            TraceAccZ3.addPoint(GloveSensors.getInstance().getSensor3().getAz().getRealSize(), GloveSensors.getInstance().getSensor3().getAz().lastElement());
            TraceAccZ4.addPoint(GloveSensors.getInstance().getSensor4().getAz().getRealSize(), GloveSensors.getInstance().getSensor4().getAz().lastElement());
            TraceAccZ5.addPoint(GloveSensors.getInstance().getSensor5().getAz().getRealSize(), GloveSensors.getInstance().getSensor5().getAz().lastElement());
            TraceAccZ6.addPoint(GloveSensors.getInstance().getSensor6().getAz().getRealSize(), GloveSensors.getInstance().getSensor6().getAz().lastElement());


        });
    }

    private void setupTraces() {
        chartGyroX.addTrace(TraceGyroX1);
        chartGyroX.addTrace(TraceGyroX2);
        chartGyroX.addTrace(TraceGyroX3);
        chartGyroX.addTrace(TraceGyroX4);
        chartGyroX.addTrace(TraceGyroX5);
        chartGyroX.addTrace(TraceGyroX6);

        chartGyroY.addTrace(TraceGyroY1);
        chartGyroY.addTrace(TraceGyroY2);
        chartGyroY.addTrace(TraceGyroY3);
        chartGyroY.addTrace(TraceGyroY4);
        chartGyroY.addTrace(TraceGyroY5);
        chartGyroY.addTrace(TraceGyroY6);

        chartGyroZ.addTrace(TraceGyroZ1);
        chartGyroZ.addTrace(TraceGyroZ2);
        chartGyroZ.addTrace(TraceGyroZ3);
        chartGyroZ.addTrace(TraceGyroZ4);
        chartGyroZ.addTrace(TraceGyroZ5);
        chartGyroZ.addTrace(TraceGyroZ6);


        chartAccX.addTrace(TraceAccX1);
        chartAccX.addTrace(TraceAccX2);
        chartAccX.addTrace(TraceAccX3);
        chartAccX.addTrace(TraceAccX4);
        chartAccX.addTrace(TraceAccX5);
        chartAccX.addTrace(TraceAccX6);

        chartAccY.addTrace(TraceAccY1);
        chartAccY.addTrace(TraceAccY2);
        chartAccY.addTrace(TraceAccY3);
        chartAccY.addTrace(TraceAccY4);
        chartAccY.addTrace(TraceAccY5);
        chartAccY.addTrace(TraceAccY6);

        chartAccZ.addTrace(TraceAccZ1);
        chartAccZ.addTrace(TraceAccZ2);
        chartAccZ.addTrace(TraceAccZ3);
        chartAccZ.addTrace(TraceAccZ4);
        chartAccZ.addTrace(TraceAccZ5);
        chartAccZ.addTrace(TraceAccZ6);

    }

    private void setupColors() {
        TraceGyroX1.setColor(Color.GREEN);
        TraceGyroX2.setColor(Color.RED);
        TraceGyroX3.setColor(Color.BLUE);
        TraceGyroX4.setColor(Color.YELLOW);
        TraceGyroX5.setColor(Color.MAGENTA);
        TraceGyroX6.setColor(Color.GRAY);

        TraceGyroY1.setColor(Color.GREEN);
        TraceGyroY2.setColor(Color.RED);
        TraceGyroY3.setColor(Color.BLUE);
        TraceGyroY4.setColor(Color.YELLOW);
        TraceGyroY5.setColor(Color.MAGENTA);
        TraceGyroY6.setColor(Color.GRAY);

        TraceGyroZ1.setColor(Color.GREEN);
        TraceGyroZ2.setColor(Color.RED);
        TraceGyroZ3.setColor(Color.BLUE);
        TraceGyroZ4.setColor(Color.YELLOW);
        TraceGyroZ5.setColor(Color.MAGENTA);
        TraceGyroZ6.setColor(Color.GRAY);

        TraceAccX1.setColor(Color.GREEN);
        TraceAccX2.setColor(Color.RED);
        TraceAccX3.setColor(Color.BLUE);
        TraceAccX4.setColor(Color.YELLOW);
        TraceAccX5.setColor(Color.MAGENTA);
        TraceAccX6.setColor(Color.GRAY);

        TraceAccY1.setColor(Color.GREEN);
        TraceAccY2.setColor(Color.RED);
        TraceAccY3.setColor(Color.BLUE);
        TraceAccY4.setColor(Color.YELLOW);
        TraceAccY5.setColor(Color.MAGENTA);
        TraceAccY6.setColor(Color.GRAY);

        TraceAccZ1.setColor(Color.GREEN);
        TraceAccZ2.setColor(Color.RED);
        TraceAccZ3.setColor(Color.BLUE);
        TraceAccZ4.setColor(Color.YELLOW);
        TraceAccZ5.setColor(Color.MAGENTA);
        TraceAccZ6.setColor(Color.GRAY);
    }
}
