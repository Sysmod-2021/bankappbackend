package database;

public abstract class LoadSaveFactory {
    public static LoadSaveStrategy fromType(LoadSaveStrategyEnum type) {
        try {
            return type.getLoadSaveClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Cannot instantiate LoadSave type");
        }
    }

}
