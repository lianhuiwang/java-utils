package codegen;

public class Variable {

  public String name;
  public String type;
  public String initCode;
  
  public Variable(String type, String name, String initCode) {
    this.type = type;
    this.name = name;
    this.initCode = initCode;
  }
  
}
