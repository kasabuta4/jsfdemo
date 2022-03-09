package com.gmail.kasabuta4.jsfdemo.covid19.view;

import static java.util.Collections.unmodifiableMap;

import com.gmail.kasabuta4.jsfdemo.common.facade.SimpleSearchFacade;
import com.gmail.kasabuta4.jsfdemo.common.view.TableView;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.CommonNumberFormat;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.WorkbookModel;
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlConverters;
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlSimpleTable;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesSimpleTableFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class MonthlyNewCasesSimpleTableView
    extends TableView<
        SearchCondition, MonthlyNewCases, List<MonthlyNewCases>, HtmlSimpleTable<MonthlyNewCases>> {

  private static final Map<String, String> PREFECTURE_MAP = prefectureMap();

  @Inject MonthlyNewCasesSimpleTableFacade facade;
  @Inject Logger logger;

  public MonthlyNewCasesSimpleTableView() {
    super(new SearchCondition());
  }

  @Override
  protected SimpleSearchFacade<SearchCondition, List<MonthlyNewCases>, MonthlyNewCases>
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
  protected boolean isEmpty(List<MonthlyNewCases> searchResult) {
    return searchResult.isEmpty();
  }

  @Override
  protected HtmlSimpleTable<MonthlyNewCases> createHtmlTable(List<MonthlyNewCases> result) {
    return new HtmlSimpleTable<>(result)
        .caption("Covid-19 Monthly New Cases")
        .tableClass("monthlyNewCases")
        .addSequenceColumn("Seq")
        .headerColumn(true)
        .endSimpleColumn()
        .addSimpleColumn("年月", MonthlyNewCases::getYearMonth)
        .converter(HtmlConverters.スラッシュ区切り年月())
        .columnClass("yearMonth")
        .endSimpleColumn()
        .addSimpleColumn("都道府県", MonthlyNewCases::getPrefecture)
        .converter(MonthlyNewCasesSimpleTableView::convertPrefecture)
        .endSimpleColumn()
        .addSimpleColumn("新規感染者数", MonthlyNewCases::getCases)
        .converter(HtmlConverters.桁区切り整数())
        .headerCellClass("headerForInteger")
        .bodyCellClass("integer")
        .endSimpleColumn();
  }

  @Override
  protected WorkbookModel createWorkbookModel(List<MonthlyNewCases> result) {
    return new WorkbookModel("MonthlyNewCases.xlsx")
        .addSimpleTable("list", "Covid-19", result)
        .captionPosition(1, 1)
        .captionSize(2, 4)
        .headerStartPosition(5, 1)
        .addSequenceColumn("Seq", 4, CommonNumberFormat.整数)
        .endSimpleColumn()
        .addYearMonthColumn("年月", MonthlyNewCases::getYearMonth)
        .endSimpleColumn()
        .addStringColumn("都道府県", MonthlyNewCases::getPrefecture, 8)
        .converter(MonthlyNewCasesSimpleTableView::convertPrefecture)
        .endSimpleColumn()
        .addIntegerColumn("新規感染者数", MonthlyNewCases::getCases, 12, CommonNumberFormat.桁区切り整数)
        .endSimpleColumn()
        .endSimpleTable();
  }

  private static String convertPrefecture(String prefecture) {
    return PREFECTURE_MAP.get(prefecture);
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
