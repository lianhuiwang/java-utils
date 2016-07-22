package codegen;

public class PrintCollector implements Collector {

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setup(Collector outputCollector) {
    // TODO Auto-generated method stub

  }

  @Override
  public void collect(Object record) {
    System.out.println("record==" + record);
  }

  @Override
  public void processRecord() {
    // TODO Auto-generated method stub

  }

}
