publicclassMyPluginextendsPlugin {

	@Override
	public void onModule(RestModule restModule) {
		restModule.addRestAction(MyRestAction.class);
	}
}
