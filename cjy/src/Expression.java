//import javafx.util.Pair;
//import jdk.internal.util.xml.impl.Pair;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private Comparator<BigInteger> keyComparator = Comparator.reverseOrder();

    private Map<BigInteger, BigInteger> itemMap
            = new TreeMap<>(keyComparator);

    public Expression() {

    }

    void insert(Item item) {
        Object key = item.getDegree();
        if (itemMap.containsKey(key)) {
            Object value = itemMap.get(key);
            itemMap.replace((BigInteger) key, ((BigInteger) value).add(item.getCoefficient()));
        }
        else {
            itemMap.put((BigInteger) key, ((BigInteger) item.getCoefficient()));
        }
    }

    public Expression(String str) {
        Pattern pattern = Pattern.compile(new Signal().getregex() + "?"
                + new BlankItem().getregex() +
                            new Item().getregex());
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String str1 = matcher.group();
            Pattern pattern1 = Pattern.compile(new Item().getregex());
            Matcher matcher1 = pattern1.matcher(str1);
            matcher1.find();
            String str2 = matcher1.group();
            Item item = new Item(str2);
            if (str1.charAt(0) == '-' && !str1.equals(str2)) {
                item.setCoefficient(item.getCoefficient().negate());
            }
            if (!item.getCoefficient().equals(new BigInteger("0"))) {
                this.insert(item);
            }
        }
    }

    Expression derive() {
        Expression result = new Expression();
        Iterator<BigInteger> iterator = itemMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Object value = itemMap.get(key);
            if (!key.equals(new BigInteger("0")) && !value.equals(new BigInteger("0"))) {
                Item item = new Item(((BigInteger)key).add(new BigInteger("-1"))
                        , ((BigInteger) value).multiply((BigInteger) key));
                result.insert(item);
            }
        }
        return result;
    }

    public String toString() {
        if (itemMap.size() == 0) {
            return "0";
        }
/*        ArrayList<Pair<BigInteger, BigInteger>> arrayList = new ArrayList<>();
        Iterator<BigInteger> iterator = itemMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            BigInteger value = itemMap.get(key);
            Pair<BigInteger,BigInteger> pair = new Pair(value, key);
            arrayList.add(pair);
        }

        Collections.sort(arrayList, (o1, o2) -> {
            if (o1.getKey().compareTo(o2.getKey()) != 0) {
                return o2.getKey().compareTo(o1.getKey());
            }
            return o2.getValue().compareTo(o1.getValue());
        });

        int len = arrayList.size();
        String ret = "";
        for (int i = 0; i < len; i++) {
            Object key = arrayList.get(i).getKey();
            Object value = arrayList.get(i).getValue();
            Item item = new Item((BigInteger) value, (BigInteger) key);
            if (i == 0) {
                ret = ret + item.toString();
            }
            else {
                if (item.getCoefficient().compareTo(new BigInteger("0")) >= 0) {
                    ret = ret + "+" + item.toString();
                }
                else {
                    ret = ret + item.toString();
                }
            }
        }*/
        Iterator<BigInteger> iterator = itemMap.keySet().iterator();
        boolean first = true;
        String ret = "";
        while (iterator.hasNext()) {
            Object key = iterator.next();
            BigInteger value = itemMap.get(key);
            Item item = new Item((BigInteger) key, value);
            if (first == true) {
                ret = ret + item.toString();
                first = false;
            }
            else {
                if (item.getCoefficient().compareTo(new BigInteger("0")) >= 0) {
                    ret = ret + "+" + item.toString();
                }
                else {
                    ret = ret + item.toString();
                }
            }
        }
        return ret;
    }
}
