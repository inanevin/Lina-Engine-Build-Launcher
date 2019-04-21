package sample;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StreamGobbler extends Thread {


    private InputStream inputStream;
    private ProgressForm progressForm;
    private Main main;

    public InputStream getConsumers() { return inputStream; }

    public StreamGobbler(InputStream inputStream,  Main m, ProgressForm form) {
        this.inputStream = inputStream;
        //this.consumer = consumer;
        this.main = m;
        this.progressForm = form;
    }

    @Override
    public void run() {
        //new BufferedReader(new InputStreamReader(inputStream)).lines()
               // .forEach(consumer);
        try {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
            {
                progressForm.UpdateInputFeed(line);
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() {
                        UpdateTextArea();
                    }
                });

            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void UpdateTextArea()
    {
        main.UpdateTextArea();
    }
}