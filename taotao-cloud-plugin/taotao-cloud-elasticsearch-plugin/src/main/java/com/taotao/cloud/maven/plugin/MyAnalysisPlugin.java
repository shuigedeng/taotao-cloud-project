public class MyAnalysisPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

        extra.put("demo_tokenizer", new AnalysisModule.AnalysisProvider<TokenizerFactory>() {
            @Override
            public TokenizerFactory get(IndexSettings indexSettings, Environment environment, String name, Settings settings) throws IOException {
                return MyTokenizerFactory.getTokenizerFactory(indexSettings, environment, name, settings);
            }
        });

        return extra;
    }
    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("demo_analyzer", new AnalysisModule.AnalysisProvider() {
            @Override
            public Object get(IndexSettings indexSettings, Environment environment, String name, Settings settings) throws IOException {
                return MyAnalyzerProvider.getAnalyzerProvider(indexSettings, environment, name, settings);
            }
        });
        return extra;
    }
}
