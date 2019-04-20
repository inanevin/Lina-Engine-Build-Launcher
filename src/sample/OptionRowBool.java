package sample;

public class OptionRowBool {

    private String m_ID = null;
    private boolean m_Value;

    public OptionRowBool() {
    }

    public OptionRowBool(String id, boolean value) {
        m_ID = id;
        m_Value = value;
    }

    public String getID() {
        return m_ID;
    }

    public void setID(String id)
    {
        m_ID = id;
    }

    public boolean getValue() {
        return m_Value;
    }

    public void setValue(boolean val) {
        m_Value = val;
    }
}