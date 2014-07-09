package nebula.plugin.override.converter

import org.apache.commons.beanutils.ConvertUtilsBean

class ApacheCommonsTypeConverter implements TypeConverter {
    ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean()

    @Override
    Object convert(String value, Class<?> clazz) {
        convertUtilsBean.convert(value, clazz)
    }
}