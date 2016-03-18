# InvestmentAlgorithm
Algorithm attempts at creating an AI which will analyze stock markets. 
Trying to optimize profitting of buying and selling shares. 

# Setup
- Fork project to own repository on GitHub. Then pull to local repository if you like.
- Create your own <Name>Investment.java class which extends Investment.

```
public class ToonwireInvestment extends Investment {...}
```
- Add unimplemented methods dictated by the Investment class
```
public class ToonwireInvestment extends Investment {

      public String getInvestmentName() {
            return "NameGoesHere";
      }
      public TradeAction tick(double price) {
            // investment algorithm goes here
            
            return someTradeAction   // TradeAction.BUY, TradeAction.SELL, TradeAction.DO_NOTHING
      }
}
```

`TradeAction.DO_NOTHING` does nothing on the tick.

`TradeAction.BUY` buys a stock, if you dont have one already.

`TradeAction.SELL` sells your stock, if you have one, otherwise it does nothing.
- Make a **pull request** when wanting to share your changes.
