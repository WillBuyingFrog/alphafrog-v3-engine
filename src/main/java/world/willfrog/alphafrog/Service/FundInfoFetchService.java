package world.willfrog.alphafrog.Service;

public interface FundInfoFetchService {

    int directFetchFundInfoByMarket(String market, int offset, int limit);

    int directFetchFundInfoByTsCode(String tsCode, int offset, int limit);
}
