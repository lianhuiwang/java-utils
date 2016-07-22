package codegen;

public interface Collector {

  void init();
  
  void setup(Collector outputCollector);
  
  void collect(Object record);
  
  void processRecord();
}
