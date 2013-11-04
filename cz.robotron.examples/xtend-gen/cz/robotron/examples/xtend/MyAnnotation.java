package cz.robotron.examples.xtend;

public @interface MyAnnotation {
  public String[] value();
  public boolean isTricky() default false;
  public int[] lotteryNumbers() default { 42, 137 };
}
