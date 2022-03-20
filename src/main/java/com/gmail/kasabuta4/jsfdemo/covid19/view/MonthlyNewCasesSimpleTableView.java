package com.gmail.kasabuta4.jsfdemo.covid19.view;

import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidths.AUTO_SIZE;
import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidths.byCharacters;
import static java.util.Collections.unmodifiableMap;

import com.gmail.kasabuta4.jsfdemo.common.facade.SimpleSearchFacade;
import com.gmail.kasabuta4.jsfdemo.common.view.TableView;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.CellStyles;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.Colors;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.Fonts;
import com.gmail.kasabuta4.jsfdemo.common.view.excel.WorkbookModel;
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlConverters;
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlSimpleTable;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesSimpleTableFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
        .columnClass("seqColumn")
        .headerCellClass("seqHeader")
        .bodyCellClass("seqBody")
        .endColumn()
        .addSimpleColumn("年月", MonthlyNewCases::getYearMonth)
        .converter(HtmlConverters.年月())
        .columnClass("yearMonthColumn")
        .headerCellClass("yearMonthHeader")
        .bodyCellClass("yearMonthBody")
        .endColumn()
        .addSimpleColumn("都道府県", MonthlyNewCases::getPrefecture)
        .converter(MonthlyNewCasesSimpleTableView::convertPrefecture)
        .columnClass("都道府県Column")
        .headerCellClass("都道府県Header")
        .bodyCellClass("都道府県Body")
        .endColumn()
        .addSimpleColumn("新規感染者数", MonthlyNewCases::getCases)
        .converter(HtmlConverters.桁区切り整数())
        .columnClass("新規感染者数Column")
        .headerCellClass("新規感染者数Header")
        .bodyCellClass("新規感染者数Body")
        .endColumn();
  }

  @Override
  protected WorkbookModel createWorkbookModel(List<MonthlyNewCases> result) {
    return new WorkbookModel("MonthlyNewCases.xlsx", MonthlyNewCasesSimpleTableView::createStyleMap)
        .addSimpleTable("list", result)
        .headerStyleKey("header")
        .bodyStyleKeys(Arrays.asList("odd", "even"))
        .addCaption("Covid-19")
        .captionStyleKey("caption")
        .endCaption()
        .addSequenceColumn("Seq", AUTO_SIZE)
        .endColumn()
        .addColumn("年月", MonthlyNewCases::getYearMonth, byCharacters(7))
        .bodyStyleKeys(Arrays.asList("yearMonth:odd", "yearMonth:even"))
        .endColumn()
        .addColumn("都道府県", MonthlyNewCases::getPrefecture, byCharacters(8))
        .converter(MonthlyNewCasesSimpleTableView::convertPrefecture)
        .endColumn()
        .addColumn("新規感染者数", MonthlyNewCases::getCases, byCharacters(12))
        .bodyStyleKeys(Arrays.asList("桁区切り整数:odd", "桁区切り整数:even"))
        .endColumn()
        .endTable();
  }

  private static Map<String, XSSFCellStyle> createStyleMap(XSSFWorkbook workbook) {
    XSSFFont captionFont = Fonts.boldOfSize(workbook, 14);
    XSSFFont headerFont = Fonts.bold(workbook);

    XSSFColor evenFillColor = Colors.create(workbook, (byte) 224, (byte) 255, (byte) 255);

    XSSFCellStyle standardStyle = workbook.getStylesSource().getStyleAt(0);

    Map<String, XSSFCellStyle> styleMap = new HashMap<>();
    styleMap.put("caption", CellStyles.create(workbook, captionFont));
    styleMap.put("header", CellStyles.create(workbook, headerFont));
    styleMap.put("odd", standardStyle);
    styleMap.put("even", CellStyles.create(workbook, evenFillColor));
    styleMap.put("yearMonth:odd", CellStyles.create(workbook, "yyyy/mm"));
    styleMap.put("yearMonth:even", CellStyles.create(workbook, "yyyy/mm", evenFillColor));
    styleMap.put("桁区切り整数:odd", CellStyles.create(workbook, "#,##0"));
    styleMap.put("桁区切り整数:even", CellStyles.create(workbook, "#,##0", evenFillColor));
    return Collections.unmodifiableMap(styleMap);
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
