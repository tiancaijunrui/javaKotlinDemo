package com.zcj.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.internal.win32.SHELLEXECUTEINFO;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @Since2017/7/18 ZhaCongJie@HF
 */
public class test3 {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setSize(200,300);
        ScrolledComposite scroll = new ScrolledComposite(shell,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        Composite composite = new Composite(scroll,SWT.NULL);
        composite.setLayout(new RowLayout());
        new Button(composite,SWT.PUSH).setText("one");
        new Button(composite,SWT.PUSH).setText("two");
        new Button(composite,SWT.PUSH).setText("three");
        new Button(composite,SWT.PUSH).setText("four");
        scroll.setContent(composite);
        composite.setSize(composite.computeSize(SWT.DEFAULT,SWT.DEFAULT));

        scroll.setContent(composite);
        composite.setSize(composite.computeSize(SWT.DEFAULT,SWT.DEFAULT));
        shell.open();
        while (!shell.isDisposed()){
            if (!display.readAndDispatch()){
                display.sleep();
            }
        }
        display.dispose();
    }



}
