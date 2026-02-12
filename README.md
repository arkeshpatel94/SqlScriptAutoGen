# SqlScriptAutoGen

Finacle Customization Code Generator for the **InspiriSys UCO/BOI Porting Project**.

A Java application that auto-generates Infosys Finacle banking platform customization artifacts from a `~` delimited input file:

- **SQL Scripts** — CREATE TABLE, INDEX, SYNONYM, GRANT statements
- **Backend Scripts** — Finacle `.scr` scripts (Fetch, Submit, SRV)
- **Front-End Files** — JSP pages, JavaScript (UI rendering, validation, resource strings), XML config

## Quick Start

### Prerequisites

- Java 8+ runtime

### Build

```bash
javac -d bin src/com/minorks/finAutomation/*.java
```

### Run

**Custom Menu** (generates 13 front-end files):
```bash
java -cp bin com.minorks.finAutomation.ScriptGeneration <inputFile> <outputPath>
```

**Product Menu** (generates single JSP):
```bash
java -cp bin com.minorks.finAutomation.ScriptGeneration <inputFile> <srvFile> <outputPath>
```

**Example:**
```bash
java -cp bin com.minorks.finAutomation.ScriptGeneration Skeleton/tableDetails_RSBCL.txt Skeleton/srvDetails_RSBCL.txt output/
```

## Input File Format

Delimiter: `~` (tilde) — 21 columns per field row, backward compatible (new columns are optional).

### Row 0 — Table & Menu Config
```
tableName~synonymName~isModTableReqd(Y/N)~isCustomMenu(Y/N)~menuName~isFuncCodePresent(Y/N)
```

### Row 1 — Script & Page Config
```
repName~className~fetchScrName~submitScrName~pageType(2page/3page)
```

### Row 2+ — Field Definitions (21 columns)
```
idName~fldName~dataType~length~isKeyFld~isCustomFld~pageName~literalName~mandatory~fieldSize~maxLength~readOnly~defaultValue~fieldType~validationType~onBlurFunction~searcherConfig~customValidation~htmlAttributes~sectionName~layoutPosition
```

| Column | Name | Description |
|--------|------|-------------|
| 1 | idName | JavaScript/HTML element ID |
| 2 | fldName | Database column name |
| 3 | dataType | Oracle data type (VARCHAR2, NUMBER, DATE) |
| 4 | length | Column length (e.g., `50 CHAR`, `20,4`) |
| 5 | isKeyFld | `Y` = criteria page, `N` = details page |
| 6 | isCustomFld | `Y` = include in custom fields |
| 7 | pageName | Page name (Product Menu only) |
| 8 | literalName | Display label for the field |
| 9 | mandatory | `Y`/`N` — mandatory field indicator |
| 10 | fieldSize | HTML input size attribute |
| 11 | maxLength | HTML maxlength attribute |
| 12 | readOnly | `Y`/`N` — read-only field |
| 13 | defaultValue | Default value for the field |
| 14 | fieldType | `a`=text, `b`/`c`=searcher, `d`=dropdown, `e`=date, `f`=radio, `g`=checkbox, `h`=textarea |
| 15 | validationType | `NUM`, `ALPHA`, `ALPHANUM`, or empty |
| 16 | onBlurFunction | Custom onBlur handler name |
| 17 | searcherConfig | Searcher function name for type `b`/`c` |
| 18 | customValidation | Custom validation function (or `AFTER_BOD`/`BEFORE_BOD` for dates) |
| 19 | htmlAttributes | Extra HTML attributes |
| 20 | sectionName | Section group name for sub-headers |
| 21 | layoutPosition | `L`/`R`/`FULL` for side-by-side field layout |

### Field Type Encoding

Dropdown, radio, and checkbox values are embedded in column 14. Supports `value:label` pairs:

```
dCash,Transfer,NEFT           # labels = values
dC:Cash,T:Transfer,N:NEFT     # separate submit values and display labels
fYes,No                       # radio buttons
gOption1,Option2              # checkboxes
```

### Example Input

```
CUST_RSBCL_TBL~C_RSBCL~Y~Y~rsbcl~Y
CUST~RSBCL~RSBCL_Crit.scr~RSBCL_Submit.scr~3page
licenseCode~LICENSE_CODE~VARCHAR2~50 CHAR~Y~Y~~License Code~Y~20~50~N~~a~ALPHANUM~~~~~~
challanNum~CHALLAN_NUM~VARCHAR2~50 CHAR~Y~Y~~Challan Number~Y~20~50~N~~a~NUM~~~~~~
liceseAcct~LICESE_ACCT~VARCHAR2~50 CHAR~N~Y~~License Account~Y~20~50~N~~b~~~liceseAcctSearcher~~~
frmDate~FRM_DATE~DATE~~N~Y~~From Date~Y~10~10~N~~e~~~~AFTER_BOD~~Details~L
toDate~TO_DATE~DATE~~N~Y~~To Date~N~10~10~N~~e~~~~BEFORE_BOD~~Details~R
tranType~TRAN_TYPE~VARCHAR2~10 CHAR~N~Y~~Transaction Type~Y~10~10~N~~dC:Cash,T:Transfer,N:NEFT,R:RTGS~~~~~~Payment~FULL
commAmt~COMM_AMT~NUMBER~20,4~N~Y~~Commission Amount~N~15~20~N~~a~NUM~~~~~Payment~L
challanAmt~CHALLAN_AMT~NUMBER~20,4~N~Y~~Challan Amount~Y~15~20~N~~a~NUM~~~~~Payment~R
challanDate~CHALLAN_DATE~DATE~~N~Y~~Challan Date~N~10~10~N~~e~~~~~~Payment~FULL
remarks~REMARKS~VARCHAR2~200 CHAR~N~Y~~Remarks~N~40~200~N~~h~~~~~~
```

## Generated Output

### SQL Scripts
- `{tableName}_Create.sql` — CREATE TABLE + INDEX + SYNONYM + GRANT
- `{tableName}_MOD_Create.sql` — MOD table (if `isModTableReqd=Y`)

### Backend Scripts
- `{fetchScrName}` — Fetch script (.scr)
- `{submitScrName}` — Submit script (.scr)

### Front-End Files (Custom Menu — up to 13 files)

| File | Purpose |
|------|---------|
| `GroupXML/{name}.xml` | Page config, invocations, field list |
| `props/{name}props.js` | Field MANDATORY/ENABLED properties |
| `{name}/{name}_crit_ginc.jsp` | Criteria page JSP |
| `{name}/{name}_det_ginc.jsp` | Details page JSP (3-page only) |
| `{name}/{name}_res_ginc.jsp` | Result page JSP |
| `javascripts/jspjs/INFENG/{name}_*_INFENG.js` | Internationalized literal strings |
| `javascripts/{name}/{name}_*_glink.js` | UI rendering using `cd_atf_functions` helpers |
| `javascripts/{name}/{name}_*_link.js` | Client-side validation |

The `glink.js` files use Finacle's `cd_atf_functions` helper library:
- `addCustomTextField()`, `addCustomDateField()`, `addCustomDropDown()`, etc.
- `setTableHeader()` / `setTableFooter()` for table structure
- `rowStart()` / `rowEnd()` / `columnAlignment()` for layout
- `addSubHeader()` for section grouping
- `checkFieldMandatory()`, `fnBlockSpecialCharacters()` for validation

## Architecture

```
ScriptGeneration.java (entry point)
  └── AutoSourceGen.generateSource()
        ├── WriteFile          → SQL scripts + .scr backend scripts
        └── FrontEndGeneration → JSP, JS, XML front-end files
```

### Source Files

| File | Purpose |
|------|---------|
| `ScriptGeneration.java` | Main class, CLI argument parsing |
| `AutoSourceGen.java` | Orchestrator |
| `InputFileRead.java` | Input file parser (21-column, backward compatible) |
| `WriteFile.java` | SQL + backend script generator |
| `CallingMethods.java` | Helper methods for `.scr` generation |
| `FrontEndGeneration.java` | Front-end file generator |
| `CustomizationDet.java` | Table/menu config POJO |
| `FieldDetails.java` | Field definition POJO (21 properties) |
| `SrvFieldDetails.java` | SRV script routing POJO |

## Skeleton Files

Sample input files in `Skeleton/`:
- `tableDetails_RSBCL.txt` — Custom Menu example (21 columns)
- `tableDetails_CCY.txt` — Product Menu example
- `srvDetails_RSBCL.txt` — SRV routing example
- `CMFetch_*` / `CMSubmit_*` — Custom Menu script skeletons
- `PMFetch_*` / `PMSubmit_*` — Product Menu script skeletons
