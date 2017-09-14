package lab3;

public class Rectangle {
	private final double width;
	private final double height;
	public Rectangle(double width, double height)
	{
		this.width = width;
		this.height=height;
	}
	public double getArea()
	{
		return this.width*this.height;
	}
	public double getPerimeter()
	{
		return 2*this.width+2*this.height;
	}
	public static void main(String[] args) throws Exception
	{
		Rectangle r = new Rectangle(10, 5);
		System.out.println(r.getArea());  //50
		System.out.println(r.getPerimeter());  // 30
	}

}
