public class MyPlugin extends Plugin {
 
    @Override
    public void onModule(AnalysisModule analysisModule) {
        analysisModule.addTokenizer("lowercase", LowercaseTokenizerFactory::new);
    }

	@Override
	public void onIndexModule(IndexModule indexModule) {
		indexModule.addIndexStore("file", FileIndexStorePlugin::new);
	}
	@Override
	public Map<String, AggregationSpec> getAggregations() {
		Map<String, AggregationSpec> aggregations = new HashMap<>();
		aggregations.put("city_population", new CityPopulationAggregationSpec());
		return aggregations;
	}

}
