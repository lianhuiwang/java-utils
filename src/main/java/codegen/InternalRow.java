package codegen;

public class InternalRow {

  private Object[] fields = null;
      
  public InternalRow(int numFields) {
    this.fields = new Object[numFields];
  }
  
  public InternalRow(Object[] values) {
    this.fields = values;
  }

  public int length() {
    return fields.length;
  }
  
  public Object get(int i) {
    return fields[i];
  }
  
  public Object getObject(int i) {
    return fields[i];
  }
  
  public int getInt(int i) {
    return (int)fields[i];
  }
  
  public long getLong(int i) {
    return (long)fields[i];
  }
  
  public void set(int i, Object value) {
    fields[i] = value;
  }

  public String toString() {
    String temp = "";
    for (int i = 0; i< fields.length; i++) {
      if (i != 0) {
        temp += ",";
      }
      temp += fields[i];
     
    }
    return temp;
  }
      
}
