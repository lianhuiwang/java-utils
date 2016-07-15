package codegen;

public class Variable {

  public String name;
  public String type;
  public String initCode;
  
  public Variable(String name, String type, String initCode) {
    this.name = name;
    this.type = type;
    this.initCode = initCode;
  }
  
}
