# SqlScriptAutoGen - Finacle Customization Code Generator

## Project Overview

Java application for the **InspiriSys UCO/BOI Porting Project** that auto-generates Infosys Finacle banking platform customization artifacts from a `~` delimited input file. It generates:

1. **SQL Scripts** - CREATE TABLE, INDEX, SYNONYM, GRANT statements
2. **Backend Scripts** - Finacle custom `.scr` scripts (Fetch, Submit, SRV)
3. **Front-End Files** - JSP pages, JavaScript (UI rendering, validation, resource strings), XML config

## Project Location

```
D:\Arkesh\work\Projects\InspiriSys - UCO ,BOI Porting Project\AFL Files\SqlScriptAutoGen
```

## How to Build & Run

**IDE**: Eclipse (project is in Eclipse workspace)

**Compile** (command line):
```
cd "D:\Arkesh\work\Projects\InspiriSys - UCO ,BOI Porting Project\AFL Files\SqlScriptAutoGen"
javac -d bin src\com\minorks\finAutomation\*.java
```

**Run** (Custom Menu - no SRV file needed):
```
java -cp bin com.minorks.finAutomation.ScriptGeneration <inputFile> <outputPath>
```

**Run** (Product Menu - with SRV file):
```
java -cp bin com.minorks.finAutomation.ScriptGeneration <inputFile> <srvFile> <outputPath>
```

**Example**:
```
java -cp bin com.minorks.finAutomation.ScriptGeneration Skeleton\tableDetails_RSBCL.txt Skeleton\srvDetails_RSBCL.txt output\
```

## Architecture

### Entry Point
`ScriptGeneration.java` -> `AutoSourceGen.generateSource()` -> calls `WriteFile` (SQL/scripts) + `FrontEndGeneration` (front-end files)

### Source Files (`src/com/minorks/finAutomation/`)

| File | Purpose |
|------|---------|
| `ScriptGeneration.java` | Main class, parses CLI args (inputFile, [srvFile], outputPath) |
| `AutoSourceGen.java` | Orchestrator - calls WriteFile + FrontEndGeneration |
| `InputFileRead.java` | Parses `~` delimited input file into model objects |
| `WriteFile.java` | Generates SQL scripts + Finacle `.scr` backend scripts |
| `CallingMethods.java` | Helper methods for `.scr` script generation (repCreation, buildWhereCondition, etc.) |
| `FrontEndGeneration.java` | Generates all front-end files (JSP, JS, XML) - **1465 lines** |
| `CustomizationDet.java` | POJO - table details, menu config, script names |
| `FieldDetails.java` | POJO - column definitions + front-end field properties |
| `SrvFieldDetails.java` | POJO - SRV script routing (funcCode -> script name) |

### Code Generation Pattern
All generators use `StringBuffer` to build file content, then write via `BufferedWriter`. FrontEndGeneration uses a `writeToFile()` helper that auto-creates parent directories.

### Other Resources (`src/`)

| Directory | Contents |
|-----------|----------|
| `src/cust/INFENG/scripts/` | Finacle script templates (.scr, .scl) - searchers, custom fields, license |
| `src/cust/INFENG/jasper/` | GenericRpt jasper report templates (3-7 fields) |
| `src/cust/INFENG/sql/` | SQL scripts (CreateTable, CreateView, data pack) |
| `src/cust/INFENG/com/` | replaceInFile.com utility |
| `src/custom/javascripts/` | Shared JS (advancedCustomSearcher, mrhCustomData, cd_atf_functions) |
| `src/custom/jsp/` | Shared JSP (advancedCustomSearcher, customReportPrint) |
| `Skeleton/` | Sample input files and skeleton script templates |

## Input File Format

Delimiter: `~` (tilde)

### Row 0 - Table & Menu Details
```
tableName~synonymName~isModTableReqd(Y/N)~isCustomMenu(Y/N)~menuName~isFuncCodePresent(Y/N)
```
- `isCustomMenu=Y` -> Custom Menu mode (13 standalone front-end files)
- `isCustomMenu=N` -> Product Menu mode (single JSP snippet)
- `isFuncCodePresent=Y` -> Full CRUD: A/M/V/I/D/U/X function codes
- `isFuncCodePresent=N` -> Add-only mode, no function code dropdown

### Row 1 - Script & Page Details
```
repName~className~fetchScrName~submitScrName~pageType(2page/3page)
```
- Script names used directly in XML invocations (not auto-generated)
- `3page` = criteria + details + result pages
- `2page` = criteria + result pages (all fields on criteria)

### Row 2+ - Column Details
```
idName~fldName~dataType~length~isKeyFld(Y/N)~isCustomFld(Y/N)~pageName~literalName~mandatory(Y/N)~fieldSize~maxLength~readOnly(Y/N)~defaultValue~fieldType~validationType
```

**Page Assignment (Custom Menu)**:
- `isKeyFld=Y` -> field goes on **criteria page**
- `isKeyFld=N` -> field goes on **details page**
- `funcCode` dropdown is auto-generated on criteria page (not from input)

**Field Types** (column 14):
| Code | Type | Notes |
|------|------|-------|
| `a` | Text field | Standard `<input type="text">` |
| `b` | Text + product searcher | Text field with search icon (product searcher) |
| `c` | Text + custom searcher | Text field with search icon (custom searcher) |
| `d` | Dropdown | Values comma-separated after `d`, e.g. `dCash,Transfer,NEFT` |
| `e` | Date field + picker | Uses `fdt="uidate"` with calendar icon |
| `f` | Radio buttons | Values comma-separated after `f`, e.g. `fYes,No` |
| `g` | Checkboxes | Values comma-separated after `g`, e.g. `gOption1,Option2` |
| `h` | Textarea | `<textarea>` element |

**Validation Types** (column 15):
| Code | Validation |
|------|-----------|
| `NUM` | Numeric only (`isNaN()` check) |
| `ALPHA` | Alphabetic only (`/^[a-zA-Z]*$/`) |
| `ALPHANUM` | Alphanumeric (`/^[a-zA-Z0-9]*$/`) |
| `ANY` or empty | No validation |

### Example Input (Custom Menu, 3-page)
```
CUST_RSBCL_TBL~C_RSBCL~Y~Y~rsbcl~Y
CUST~RSBCL~RSBCL_Crit.scr~RSBCL_Submit.scr~3page
licenseCode~LICENSE_CODE~VARCHAR2~50 CHAR~Y~Y~~License Code~Y~20~50~N~~a~ALPHANUM
challanNum~CHALLAN_NUM~VARCHAR2~50 CHAR~Y~Y~~Challan Number~Y~20~50~N~~a~NUM
liceseAcct~LICESE_ACCT~VARCHAR2~50 CHAR~N~Y~~License Account~Y~20~50~N~~b~
frmDate~FRM_DATE~DATE~~N~Y~~From Date~Y~10~10~N~~e~
toDate~TO_DATE~DATE~~N~Y~~To Date~N~10~10~N~~e~
tranType~TRAN_TYPE~VARCHAR2~10 CHAR~N~Y~~Transaction Type~Y~10~10~N~~dCash,Transfer,NEFT,RTGS~
commAmt~COMM_AMT~NUMBER~20,4~N~Y~~Commission Amount~N~15~20~N~~a~NUM
challanAmt~CHALLAN_AMT~NUMBER~20,4~N~Y~~Challan Amount~Y~15~20~N~~a~NUM
challanDate~CHALLAN_DATE~DATE~~N~Y~~Challan Date~N~10~10~N~~e~
remarks~REMARKS~VARCHAR2~200 CHAR~N~Y~~Remarks~N~40~200~N~~h~
```

### SRV Input File (Product Menu only, separate file)
```
funcCode~operation~fetchSrvScriptName~submitSrvScriptName
```
Example: `A~A~~SRV_AddRsbclDtl_post_process_data.scr`

## Generated Output Files

### SQL Output (both modes)
- `{tableName}_Create.sql` - CREATE TABLE + INDEX + SYNONYM + GRANT
- `{tableName}_MOD_Create.sql` - MOD table (if `isModTableReqd=Y`)

### Backend Script Output (both modes)
- `{fetchScrName}` - Fetch script (.scr)
- `{submitScrName}` - Submit script (.scr)
- SRV scripts (Product Menu only, from SRV input file)

### Front-End Output - Custom Menu (`isCustomMenu=Y`)

13 files generated (or fewer for 2-page mode):

| # | File Path | Purpose |
|---|-----------|---------|
| 1 | `GroupXML/{name}.xml` | Page config, invocations, field list |
| 2 | `props/{name}props.js` | Field MANDATORY/ENABLED properties |
| 3 | `{name}/{name}_crit_ginc.jsp` | Criteria page JSP |
| 4 | `{name}/{name}_det_ginc.jsp` | Details page JSP (3-page only) |
| 5 | `{name}/{name}_res_ginc.jsp` | Result page JSP |
| 6 | `javascripts/jspjs/INFENG/{name}_crit_INFENG.js` | Criteria page literal strings (FLT codes) |
| 7 | `javascripts/jspjs/INFENG/{name}_det_INFENG.js` | Details page literals (3-page only) |
| 8 | `javascripts/jspjs/INFENG/{name}_res_INFENG.js` | Result page literals |
| 9 | `javascripts/{name}/{name}_crit_glink.js` | Criteria page UI rendering + behavior |
| 10 | `javascripts/{name}/{name}_det_glink.js` | Details page UI rendering (3-page only) |
| 11 | `javascripts/{name}/{name}_res_glink.js` | Result page rendering |
| 12 | `javascripts/{name}/{name}_crit_link.js` | Criteria page validation |
| 13 | `javascripts/{name}/{name}_det_link.js` | Details page validation (3-page only) |

FLT resource codes auto-increment starting from `FLT900001`, reset per page type.

### Front-End Output - Product Menu (`isCustomMenu=N`)

Single file: `{pageName}crit_custom.jsp`

- Wrapped in `if(mopId == "H{MENU_NAME_UPPER}")` check
- Uses `setFieldsToCustomData()` / `getFieldsFromCustomData()` for product data exchange
- Has `_pre_ONCLICK()` and `_post_ONLOAD()` hooks
- `mopId` = "H" + menuName.toUpperCase()

## Reference Files

Reference implementations for comparing generated output:

- **Custom Menu (rsbcl)**: `C:\Users\Admin\Downloads\front-end_files\` (12 reference files)
- **Product Menu**: `C:\Users\Admin\Downloads\acmlacrit_custom.jsp`
- **Input format doc**: `C:\Users\Admin\Downloads\readMewithcustomMenu.txt`

## Finacle Scripting Conventions

Backend `.scr` scripts use Finacle custom scripting language:
- `<--START` / `END-->` block delimiters
- `TRACE ON/OFF` for debug logging
- `IF/THEN/ELSE/ENDIF`, `GOTO` for flow control
- `CREATEREP`, `CREATECLASS` for repository/class setup
- `urhk_dbSelectWithBind` - DB SELECT with bind variables
- `urhk_SetOrbOut` - Set output values
- `urhk_GetOrbIn` - Get input values

Front-end JSP/JS conventions:
- `ARJspCurr.getInput()` / `ARJspCurr.getInputWithGroup()` for JSP variable access
- `ParseValue.checkString()` for null-safe string handling
- `document.write()` based HTML rendering in glink.js files
- `jspResArr.get("FLTxxxxxx")` for internationalized literal strings
- `printBlock()`, `printFooterBlock()`, `fnOnLoad()`, `fnPopulateControlValues()` standard method pattern
- `fnValidateData()` in link.js files for client-side validation

## Skeleton Input Files

Located in `Skeleton/` directory:
- `tableDetails_CCY.txt` - Product Menu example (CCY customization)
- `tableDetails_ALLIN.txt` - Product Menu example (ALLIN)
- `tableDetails_RSBCL.txt` - Custom Menu example with front-end fields
- `srvDetails_CCY.txt` - SRV routing example
- `srvDetails_RSBCL.txt` - SRV routing for RSBCL
- `CMFetch_WithMOD` / `CMFetch_WithOutMOD` - Custom Menu fetch script skeletons
- `CMSubmit_WithMOD` / `CMSubmit_WithOutMOD` - Custom Menu submit script skeletons
- `PMFetch_WithMOD.txt` / `PmFetch_WithOutMOD.txt` - Product Menu fetch skeletons
- `PMSubmit_WithMOD.txt` / `PMSubmit_WithOutMOD.txt` - Product Menu submit skeletons
- `readMe.txt` - Original input format documentation

## Key Design Decisions

1. **Backward compatible input parsing** - New front-end fields (columns 8-15) are optional; `values.length > N` guards allow old-format input files to still work for SQL/script generation.
2. **fieldType d/f/g encoding** - Dropdown/radio/checkbox values are embedded in the fieldType column itself (e.g., `dCash,Transfer`). The parser splits the first character as type and the rest as comma-separated values.
3. **FLT counter reset per page** - Each INFENG JS file starts its own FLT counter at 900001 so codes don't conflict.
4. **isFuncCodePresent flag** controls both backend script generation (full CRUD vs Add-only) and front-end (funcCode dropdown presence, disable logic for V/I/X modes).
5. **Product Menu mopId** = `"H" + menuName.toUpperCase()` (e.g., menuName="acmla" -> mopId="HACMLA").
