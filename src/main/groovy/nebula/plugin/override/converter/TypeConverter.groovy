package nebula.plugin.override.converter

interface TypeConverter {
    Object convert(String value, Class<?> clazz)
}