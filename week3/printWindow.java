class Element{
	private String name;
	private String text;
	public Element(String name, String text) {
		this.text = text;
	}
	public print(){
	}
}
class Container extends Element{
	private Element[] elements;
	public print(){

	}
	public addElement(Element element) {

	}
}
class WindowForm extends Container{
	public WindowForm(String text) {
		super("WinForm", text);
	}
}
class Button extends Element {
	public Button(String text) {
		super("Button", text);
	}
}
class Picture extends Element{
	public Picture(String text) {
		super("Picture", text);
	}
}
class Frame extends Container{
	public Frame(String text){
		super("Frame", text);
	}
}
class Label extends Element{
	public Label(String text){
		super("Label", text);
	}
}
class TextBox extends Element{
	public TextBox(String text){
		super("TextBox", text);
	}
}
class PasswordBox extends Element{
	public PasswordBox(String text){
		super("PasswordBox", text);
	}
}
class CheckBox extends Element{
	public CheckBox(String text){
		super("CheckBox", text);
	}
}
class LinkLabel extends Element{
	public LinkLabel(String text){
		super("LinkLabel", text);
	}
}

class Demo{
	public static void main(){
		WindowForm form = new WindowForm('WINDOW窗口');

		form.addElement(new Picture("LOGO图片"));
		form.addElement(new Button("登录"));
		form.addElement(new Button("注册"));

		Frame frame = new Frame("FRAME1");
		form.addElement(frame);

		frame.addElement(new Label("用户名"));
		frame.addElement(new TextBox("文本框"));
		frame.addElement(new Label("密码"));
		frame.addElement(new PasswordBox("密码框"));
		frame.addElement(new CheckBox("复选框"));
		frame.addElement(new TextBox("记住用户名"));
		frame.addElement(new LinkLabel("忘记密码"));

		form.print();
	}
}