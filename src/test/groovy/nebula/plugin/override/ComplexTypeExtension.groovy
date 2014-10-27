package nebula.plugin.override

class ComplexTypeExtension {

    List nullUntypedStringList
    List<String> nullStringList
    List<Integer> ullIntegerList
    Set nullUntypedStringSet
    Set<String> nullStringSet
    Set<Integer> nullIntegerSet
    Map nullUntypedStringToStringMap    //Q: Do we want to support other untyped maps?
    Map<String, String> nullStringToStringMap
    Map<String, Integer> nullStringToIntegerMap
    Map<Integer, Integer> nullIntegerToIntegerMap
    Map<Integer, String> nullIntegerToStringMap

    List untypedStringList = ['str']
    List<String> stringList = ['str']
    List<Integer> integerList = [1]
    Set untypedStringSet = ['str']
    Set<String> stringSet = ['str']
    Set<Integer> integerSet = [1]
    Map untypedStringToStringMap = ['str': 'str2']    //Q: Do we want to support other untyped maps?
    Map<String, String> stringToStringMap = ['str': 'str2']
    Map<String, Integer> stringToIntegerMap = ['str': 1]
    Map<Integer, Integer> integerToIntegerMap = [(0): 1]
    Map<Integer, String> integerToStringMap = [(0): 'str2']
}
