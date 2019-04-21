package sample;

import javafx.concurrent.Task;

public class CommandTask extends Task<Void>
{

    public int m_Progress;
    public int m_MaxProgress;
    @Override
    public Void call()
    {
        while(m_Progress < m_MaxProgress)
            updateProgress(m_Progress, m_MaxProgress);
        return null;
    }
}
