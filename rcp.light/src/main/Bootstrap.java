package main;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class Bootstrap implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Headless RCP1w");
		System.in.read();
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
