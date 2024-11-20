package world.willfrog.alphafrog.Common;

import world.willfrog.alphafrog.Entity.Common.Quote;

import java.util.List;

public class QuoteAnalysisUtils {

    public static double[] normalizeDailyQuote(List<Quote> quoteList){
       int len = quoteList.size();
       double[] ret = new double[len];

       double baseDaily = quoteList.get(0).getClose();

       for(int i = 0; i < len; i++){
           ret[i] = quoteList.get(i).getClose() / baseDaily;
       }

       return ret;
    }


}
