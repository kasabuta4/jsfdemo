package com.gmail.kasabuta4.jsfdemo.covid19.view;

import static java.util.Collections.unmodifiableMap;

import com.gmail.kasabuta4.jsfdemo.common.application.SimpleSearchFacade;
import com.gmail.kasabuta4.jsfdemo.common.application.XYTableView;
import com.gmail.kasabuta4.jsfdemo.common.application.excel.CommonNumberFormat;
import com.gmail.kasabuta4.jsfdemo.common.application.excel.WorkbookModel;
import com.gmail.kasabuta4.jsfdemo.common.application.html.HtmlXYTable;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesXYTableFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class MonthlyNewCasesXYTableView
    extends XYTableView<SearchCondition, MonthlyNewCases, YearMonth, String> {

  private static final long serialVersionUID = 1L;

  private static final Set<String> 東京圏 =
      new HashSet<>(Arrays.asList("Tokyo", "Kanagawa", "Chiba", "Saitama"));
  private static final Set<String> 大阪圏 = new HashSet<>(Arrays.asList("Osaka", "Hyogo", "Kyoto"));

  private static final Map<String, String> PREFECTURE_MAP = prefectureMap();
  private static final DateTimeFormatter YEAR_MONTH_FORMATTER =
      DateTimeFormatter.ofPattern("uuuu/MM");
  private static final NumberFormat 桁区切り整数 = new DecimalFormat("#,##0");

  @Inject MonthlyNewCasesXYTableFacade facade;

  @Inject Logger logger;

  public MonthlyNewCasesXYTableView() {
    super(new SearchCondition());
  }

  @Override
  protected SimpleSearchFacade<
          SearchCondition, Map<YearMonth, Map<String, MonthlyNewCases>>, MonthlyNewCases>
      getFacade() {
    return facade;
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }

  @Override
  protected String getDestinationOnFound() {
    return "result.xhtml";
  }

  @Override
  protected HtmlXYTable<YearMonth, String, MonthlyNewCases> createHtmlXYTable(
      Map<YearMonth, Map<String, MonthlyNewCases>> data) {
    return new HtmlXYTable<>("東京圏と大阪圏の比較", data)
        .addIdentityXColumn("年月")
        .converter(MonthlyNewCasesXYTableView::convertYearMonth)
        .columnClass("yearMonth")
        .endXColumn()
        .addYColumn("東京圏", 東京圏::contains, MonthlyNewCases::getCases)
        .converter(MonthlyNewCasesXYTableView::convertInteger)
        .yTitles(Arrays.asList(MonthlyNewCasesXYTableView::convertPrefecture))
        .columnClass("東京圏colgroup")
        .endYColumn()
        .addYColumn("大阪圏", 大阪圏::contains, MonthlyNewCases::getCases)
        .converter(MonthlyNewCasesXYTableView::convertInteger)
        .yTitles(Arrays.asList(MonthlyNewCasesXYTableView::convertPrefecture))
        .columnClass("大阪圏colgroup")
        .endYColumn();
  }

  @Override
  protected WorkbookModel createWorkbookModel(Map<YearMonth, Map<String, MonthlyNewCases>> data) {
    return new WorkbookModel("東京圏と大阪圏の比較.xlsx")
        .addXYTable("比較", "東京圏と大阪圏の月間新規感染者数比較", data)
        .addYearMonthXColumn("年月", Function.identity())
        .converter(MonthlyNewCasesXYTableView::convertYearMonth)
        .endXColumn()
        .addIntegerYColumn(
            "東京圏", 東京圏::contains, MonthlyNewCases::getCases, 7, CommonNumberFormat.桁区切り整数)
        .addIdentityYTitle(CommonNumberFormat.標準)
        .converter(MonthlyNewCasesXYTableView::convertPrefecture)
        .endYTitle()
        .endYColumn()
        .addIntegerYColumn(
            "大阪圏", 大阪圏::contains, MonthlyNewCases::getCases, 7, CommonNumberFormat.桁区切り整数)
        .addIdentityYTitle(CommonNumberFormat.標準)
        .converter(MonthlyNewCasesXYTableView::convertPrefecture)
        .endYTitle()
        .endYColumn()
        .endXYTable();
  }

  private static String convertPrefecture(String prefecture) {
    return PREFECTURE_MAP.get(prefecture);
  }

  private static String convertYearMonth(YearMonth yearMonth) {
    return yearMonth == null ? null : YEAR_MONTH_FORMATTER.format(yearMonth);
  }

  private static String convertInteger(Integer i) {
    return i == null ? null : 桁区切り整数.format(i);
  }

  private static Map<String, String> prefectureMap() {
    Map<String, String> map = new HashMap<>(64);
    map.put("ALL", "全国");
    map.put("Aichi", "愛知");
    map.put("Akita", "秋田");
    map.put("Aomori", "青森");
    map.put("Chiba", "千葉");
    map.put("Ehime", "愛媛");
    map.put("Fukui", "福井");
    map.put("Fukuoka", "福岡");
    map.put("Fukushima", "福島");
    map.put("Gifu", "岐阜");
    map.put("Gunma", "群馬");
    map.put("Hiroshima", "広島");
    map.put("Hokkaido", "北海道");
    map.put("Hyogo", "兵庫");
    map.put("Ibaraki", "茨城");
    map.put("Ishikawa", "石川");
    map.put("Iwate", "岩手");
    map.put("Kagawa", "香川");
    map.put("Kagoshima", "鹿児島");
    map.put("Kanagawa", "神奈川");
    map.put("Kochi", "高知");
    map.put("Kumamoto", "熊本");
    map.put("Kyoto", "京都");
    map.put("Mie", "三重");
    map.put("Miyagi", "宮城");
    map.put("Miyazaki", "宮崎");
    map.put("Nagano", "長野");
    map.put("Nagasaki", "長崎");
    map.put("Nara", "奈良");
    map.put("Niigata", "新潟");
    map.put("Oita", "大分");
    map.put("Okayama", "岡山");
    map.put("Okinawa", "沖縄");
    map.put("Osaka", "大阪");
    map.put("Saga", "佐賀");
    map.put("Saitama", "埼玉");
    map.put("Shiga", "滋賀");
    map.put("Shimane", "島根");
    map.put("Shizuoka", "静岡");
    map.put("Tochigi", "栃木");
    map.put("Tokushima", "徳島");
    map.put("Tokyo", "東京");
    map.put("Tottori", "鳥取");
    map.put("Toyama", "富山");
    map.put("Wakayama", "和歌山");
    map.put("Yamagata", "山形");
    map.put("Yamaguchi", "山口");
    map.put("Yamanashi", "山梨");
    return unmodifiableMap(map);
  }
}
