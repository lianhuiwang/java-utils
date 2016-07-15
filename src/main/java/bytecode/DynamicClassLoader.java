package bytecode;

public class DynamicClassLoader extends ClassLoader {

  private static ClassLoader resolveClassLoader(ClassLoader parentClassLoader) {
    if (parentClassLoader == null) {
      parentClassLoader = DynamicClassLoader.class.getClassLoader();
    }
    if (parentClassLoader == null) {
      parentClassLoader = ClassLoader.getSystemClassLoader();
    }
    return parentClassLoader;
  }

  public DynamicClassLoader() {
    this(null);
  }

  public DynamicClassLoader(ClassLoader parentClassLoader) {
    super(resolveClassLoader(parentClassLoader));
  }

  public Class<?> defineClass(String className, byte[] bytecode) {
    return defineClass(className, bytecode, 0, bytecode.length);
  }

}
