package com.zcj.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @Since2017/7/18 ZhaCongJie@HF
 */
public class test2 {
    public static void main(String[] args) {
        Display display = new Display();
        final Shell shell = new Shell(display);
        Canvas canvas = new Canvas(shell, SWT.NONE);
        canvas.setSize(400,300);
        canvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                Rectangle clientArea = shell.getClientArea();
                paintEvent.gc.drawLine(0, 0, clientArea.width, clientArea.height);
                paintEvent.gc.drawLine(clientArea.width, 0, 0, clientArea.height);
            }
        });
        shell.setText("swtTest");
        shell.pack();
        shell.open();
        while (!shell.isDisposed()){
            if (!display.readAndDispatch()){
                display.sleep();
            }
        }
        display.dispose();
    }
}
