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
import com.gmail.kasabuta4.jsfdemo.common.view.html.HtmlMapTable;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesXYTableFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
public class MonthlyNewCasesMapTableView
    extends TableView<
        SearchCondition,
        MonthlyNewCases,
        Map<YearMonth, Map<String, MonthlyNewCases>>,
        HtmlMapTable<YearMonth, String, MonthlyNewCases>> {

  private static final long serialVersionUID = 1L;

  private static final Set<String> 東京圏 =
      new HashSet<>(Arrays.asList("Tokyo", "Kanagawa", "Chiba", "Saitama"));
  private static final Set<String> 大阪圏 = new HashSet<>(Arrays.asList("Osaka", "Hyogo", "Kyoto"));

  private static final Map<String, String> PREFECTURE_MAP = prefectureMap();

  @Inject MonthlyNewCasesXYTableFacade facade;

  @Inject Logger logger;

  public MonthlyNewCasesMapTableView() {
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
  protected boolean isEmpty(Map<YearMonth, Map<String, MonthlyNewCases>> result) {
    return result.isEmpty();
  }

  @Override
  protected HtmlMapTable<YearMonth, String, MonthlyNewCases> createHtmlTable(
      Map<YearMonth, Map<String, MonthlyNewCases>> data) {
    return new HtmlMapTable<>(data)
        .caption("東京圏と大阪圏の比較")
        .tableClass("東京圏と大阪圏の比較")
        .highlight(MonthlyNewCasesMapTableView::注目月, 注目月HighlightClasses())
        .addSequenceColumn("Seq")
        .headerColumn(true)
        .columnClass("seqColumn")
        .headerCellClass("seqHeader")
        .bodyCellClass("seqBody")
        .endColumn()
        .addIdentityKeyColumn("年月")
        .converter(HtmlConverters.年月())
        .headerColumn(true)
        .columnClass("yearMonthColumn")
        .headerCellClass("yearMonthHeaderCell")
        .bodyCellClass("yearMonthBodyCell")
        .endColumn()
        .addValueColumn("東京圏", MonthlyNewCases::getCases, 東京圏::contains)
        .converter(HtmlConverters.桁区切り整数())
        .columnClass("東京圏colgroup")
        .headerCellClass("東京圏HeaderCell")
        .bodyCellClass("東京圏BodyCell")
        .highlight(MonthlyNewCasesMapTableView::注目月, 注目月HighlightClasses())
        .addIdentityKeyHeader()
        .converter(MonthlyNewCasesMapTableView::convertPrefecture)
        .headerCellClass("東京圏都道府県HeaderCell")
        .endKeyHeader()
        .endColumn()
        .addValueColumn("大阪圏", MonthlyNewCases::getCases, 大阪圏::contains)
        .converter(HtmlConverters.桁区切り整数())
        .columnClass("大阪圏colgroup")
        .headerCellClass("大阪圏HeaderCell")
        .bodyCellClass("大阪圏BodyCell")
        .addIdentityKeyHeader()
        .converter(MonthlyNewCasesMapTableView::convertPrefecture)
        .headerCellClass("大阪圏都道府県HeaderCell")
        .endKeyHeader()
        .addIdentityKeyHeader()
        .headerCellClass("大阪圏PrefectureHeaderCell")
        .endKeyHeader()
        .endColumn();
  }

  @Override
  protected WorkbookModel createWorkbookModel(Map<YearMonth, Map<String, MonthlyNewCases>> data) {
    return new WorkbookModel("東京圏と大阪圏の比較.xlsx", MonthlyNewCasesMapTableView::createStyleMap)
        .standardStyleChanger(MonthlyNewCasesMapTableView::changeStandardStyle)
        .addMapTable("比較", data)
        .headerStyleKey("header")
        .bodyStyleKeys(Arrays.asList("odd", "even"))
        .highlight(MonthlyNewCasesMapTableView::注目月, 注目月HighlightKeys())
        .addCaption("東京圏と大阪圏の月間新規感染者数比較")
        .captionStyleKey("caption")
        .endCaption()
        .addSequenceColumn("Seq", AUTO_SIZE)
        .endColumn()
        .addKeyColumn("年月", Function.identity(), byCharacters(7))
        .bodyStyleKeys(Arrays.asList("yearMonth:odd", "yearMonth:even"))
        .highlightStyleKeys(注目月YearMonthHighlightKeys())
        .endColumn()
        .addValueColumn("東京圏", MonthlyNewCases::getCases, byCharacters(7), 東京圏::contains)
        .bodyStyleKeys(Arrays.asList("桁区切り整数:odd", "桁区切り整数:even"))
        .highlightStyleKeys(注目月桁区切り整数HighlightKeys())
        .addIdentityKeyHeader()
        .converter(MonthlyNewCasesMapTableView::convertPrefecture)
        .endKeyHeader()
        .endColumn()
        .addValueColumn("大阪圏", MonthlyNewCases::getCases, byCharacters(7), 大阪圏::contains)
        .bodyStyleKeys(Arrays.asList("桁区切り整数:odd", "桁区切り整数:even"))
        .addIdentityKeyHeader()
        .converter(MonthlyNewCasesMapTableView::convertPrefecture)
        .endKeyHeader()
        .addIdentityKeyHeader()
        .endKeyHeader()
        .endColumn()
        .endTable();
  }

  private static void changeStandardStyle(XSSFCellStyle standardStyle) {
    XSSFFont standardFont = standardStyle.getFont();
    standardFont.setFontName("ヒラギノ角ゴシック");
    standardFont.setFontHeightInPoints((short) 14);
  }

  private static Map<String, XSSFCellStyle> createStyleMap(XSSFWorkbook workbook) {
    XSSFColor evenFillColor = Colors.create(workbook, 224, 255, 255);
    XSSFColor redColor = Colors.create(workbook, 255, 0, 0);

    XSSFFont captionFont = Fonts.boldOfSize(workbook, 18);
    XSSFFont headerFont = Fonts.bold(workbook);
    XSSFFont redFont = Fonts.ofColor(workbook, redColor);

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
    styleMap.put("注目月:odd", CellStyles.create(workbook, redFont));
    styleMap.put("注目月:even", CellStyles.create(workbook, redFont, evenFillColor));
    styleMap.put("注目月:yearMonth:odd", CellStyles.create(workbook, "yyyy/mm", redFont));
    styleMap.put(
        "注目月:yearMonth:even", CellStyles.create(workbook, "yyyy/mm", redFont, evenFillColor));
    styleMap.put("注目月:桁区切り整数:odd", CellStyles.create(workbook, "#,##0", redFont));
    styleMap.put("注目月:桁区切り整数:even", CellStyles.create(workbook, "#,##0", redFont, evenFillColor));
    return Collections.unmodifiableMap(styleMap);
  }

  private static Object 注目月(YearMonth yearMonth) {
    return yearMonth.getMonthValue() == 4 ? Boolean.TRUE : Boolean.FALSE;
  }

  private static String convertPrefecture(String prefecture) {
    return PREFECTURE_MAP.get(prefecture);
  }

  private static Map<Object, String> 注目月HighlightClasses() {
    Map<Object, String> map = new HashMap<>(1);
    map.put(Boolean.TRUE, "watching");
    return Collections.unmodifiableMap(map);
  }

  private static Map<Object, List<String>> 注目月HighlightKeys() {
    Map<Object, List<String>> map = new HashMap<>(2);
    map.put(Boolean.TRUE, Arrays.asList("注目月:odd", "注目月:even"));
    map.put(Boolean.FALSE, Arrays.asList("odd", "even"));
    return Collections.unmodifiableMap(map);
  }

  private static Map<Object, List<String>> 注目月YearMonthHighlightKeys() {
    Map<Object, List<String>> map = new HashMap<>(2);
    map.put(Boolean.TRUE, Arrays.asList("注目月:yearMonth:odd", "注目月:yearMonth:even"));
    map.put(Boolean.FALSE, Arrays.asList("yearMonth:odd", "yearMonth:even"));
    return Collections.unmodifiableMap(map);
  }

  private static Map<Object, List<String>> 注目月桁区切り整数HighlightKeys() {
    Map<Object, List<String>> map = new HashMap<>(2);
    map.put(Boolean.TRUE, Arrays.asList("注目月:桁区切り整数:odd", "注目月:桁区切り整数:even"));
    map.put(Boolean.FALSE, Arrays.asList("桁区切り整数:odd", "桁区切り整数:even"));
    return Collections.unmodifiableMap(map);
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
