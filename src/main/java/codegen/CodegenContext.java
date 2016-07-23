package codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CodegenContext {
  
  public String INPUT_ROW = "i";
  
  public ExprCode[] currentVars = null;
  
  // 维护变量名与对象关系
  public List<Object> references = new ArrayList<Object>();
  
  // 维持变量名更新
  private Map<String, Integer> freshNameIds = new HashMap<String, Integer>();
  
  // 变量列表
  private List<Variable> variables = new ArrayList<Variable>();
  
  private Map<String, String> addedFunctions = new HashMap<String, String>();
  
  public void addNewFunction(String funcName, String funcCode) {
    this.addedFunctions.put(funcName, funcCode);
  }
  
  public String addReferenceObj(String name, Object obj, String className) {
    String term = freshName(name);
    int idx = references.size();
    references.add(obj);
    if (className == null) {
      className = obj.getClass().getName();
    }
    addMutableState(className, term, term + " = (" + className + ") references[" + idx + "];"); 
    return term;
  }
  
  public void addMutableState(String className, String term, String initCode) {
   this.variables.add(new Variable(className, term, initCode));
  }

  public String freshName(String name) {
    if (this.freshNameIds.containsKey(name)) {
      int id = this.freshNameIds.get(name);
      this.freshNameIds.put(name, id + 1);
      return name + id;
    } else {
      this.freshNameIds.put(name, 1);
      return name;
    }
  }
  
  public String declareMutableStates() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.variables.size(); i++) {
      Variable variable = this.variables.get(i);
      if (i != 0) {
        sb.append("\n");
      }
      sb.append("private ").append(variable.type).append(" ").append(variable.name).append(";");
    }
    return sb.toString();
  }
  
  public String initMutableStates() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.variables.size(); i++) {
      Variable variable = this.variables.get(i);
      if (i != 0) {
        sb.append("\n");
      }
      sb.append(variable.initCode);
    }
    return sb.toString();
  }
  
  public String declareAddedFunctions() {
    boolean start = false;
    Iterator<String> functions = this.addedFunctions.values().iterator();
    StringBuilder sb = new StringBuilder();
    while (functions.hasNext()) {
      if (start) {
        sb.append("\n");
      } else {
        start = true;
      }
      sb.append(functions.next());
    }
    return sb.toString();
  }

  public String javaType(DataType dataType) {
    switch(dataType) {
    case IntType:
      return "int";
    case LongType:
      return "long";
    }
    return "Object";
  }
  
  public String primitiveTypeName(DataType dataType) {
    switch(dataType) {
    case IntType:
      return "Int";
    case LongType:
      return "Long";
    }
    return "Object";
  }
}
