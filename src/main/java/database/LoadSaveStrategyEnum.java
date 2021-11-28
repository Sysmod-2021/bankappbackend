package database;

public enum LoadSaveStrategyEnum {
//    EXCEL(TransactionExcelLoadSave.class),
    TEXT(DatabaseTextLoadSave.class);
//    CSV(TransactionCSVLoadSave.class);

    private final Class<? extends LoadSaveStrategy> loadSaveClass;

    LoadSaveStrategyEnum(Class<? extends LoadSaveStrategy> loadSaveClass) {
        this.loadSaveClass = loadSaveClass;
    }

    public Class<? extends LoadSaveStrategy> getLoadSaveClass() {
        return this.loadSaveClass;
    }
}
