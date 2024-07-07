public final class MyTokenizer extends Tokenizer {
    //词元文本属性
    private final CharTermAttribute termAtt;
    //词元位移属性
    private final OffsetAttribute offsetAtt;
    // 距离
    private final PositionIncrementAttribute positionAttr;

    /**
     * 单文档当前所在的总offset，当reset（切换multi-value fields中的value）的时候不清零，在end（切换field）时清零
     */
    private int totalOffset = 0;

    private AnalyzeContext analyzeContext;

    public MyTokenizer(Configuration configuration) {
        super();
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        positionAttr = addAttribute(PositionIncrementAttribute.class);

        analyzeContext = new AnalyzeContext(input, configuration);
    }

    /**
     * @return 返会true告知还有下个词元，返会false告知词元输出完毕
     * @throws IOException
     */
    @Override
    public boolean incrementToken() throws IOException {
        this.clearAttributes();

        int position = 0;
        Term term;
        boolean unIncreased = true;
        do {
            term = analyzeContext.next();
            if (term == null) {
                break;
            }
            if (TextUtility.isBlank(term.getText())) { // 过滤掉空白符，提高索引效率
                continue;
            }

            ++position;
            unIncreased = false;
        } while (unIncreased);

        if (term != null) {
            positionAttr.setPositionIncrement(position);
            termAtt.setEmpty().append(term.getText());
            offsetAtt.setOffset(correctOffset(totalOffset + term.getOffset()),
                    correctOffset(totalOffset + term.getOffset() + term.getText().length()));
            return true;
        } else {
            totalOffset += analyzeContext.offset;
            return false;
        }
    }
    
    @Override
    public void end() throws IOException {
        super.end();
        offsetAtt.setOffset(totalOffset, totalOffset);
        totalOffset = 0;
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        analyzeContext.reset(new BufferedReader(this.input));
    }
}
