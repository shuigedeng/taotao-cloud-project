import com.sun.source.util.Plugin;

public class MyPlugin11 extends Plugin {

	@Override
	public void onModule(RestModule restModule) {
		restModule.addRestAction(MyRestAction.class);
	}
}
