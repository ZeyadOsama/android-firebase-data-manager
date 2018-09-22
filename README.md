# Firebase Data Manager
Easily managing generic objects which are retrevied from Firebase

[![license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/ChadCSong/ShineButton/raw/master/LICENSE)
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)

## Installation
Gradle:
```groovy
'com.github.ZeyadOsama:FirebaseDataManager-Android:Beta'
```

## Usage


* Implement `DatabaseAccessibleObject` in any class that you want to be queried
```java
public class Foo implements DatabaseAccessibleObject {
    
    @Override
    public String getDatabaseReference() {
        // return your own variable
    }

    @Override
    public void setDatabaseReference(String databaseReference) {
        // set your own variable
    }
}
```


* Create your data manager class for your class and extend `DataManager<T extends DatabaseAccessibleObject>`
```java
public class FooDataManager extends DataManager<Foo> {

}
```


* Add a constructor matching super, where it takes a parameter `Foo.class`
```java
public FooDataManager() {
    super(Foo.class);
}
```


* Implement `ViewModelInterface<DatabaseAccessibleObject>` in your `ViewModel`
```java
public class FooViewModel extends ViewModel implements ViewModelInterface {

}
```


* Create an instance of your `DataManager` and a `MediatorLiveData` variable in your `ViewModel`
```java
DataManager dataManager = new FooDataManager();
MediatorLiveData<List<Foo>> mediator = new MediatorLiveData<>();
```


* Override `ViewModelInterface` methods in the following way
```java
@Override
public void writeToDatabase(Education education) {
    dataManager.writeToDatabase(education);
}

@Override
public void writeToDatabase(Education education, String databasePath) {
    dataManager.writeToDatabase(education, databasePath);
}

@Override
public void editInDatabase(Education education, String databasePath) {
    dataManager.editInDatabase(education, databasePath);
}

@Override
public void removeFromDatabase(String databasePath) {
    dataManager.removeFromDatabase(databasePath);
}

@Override
public LiveData<List<Foo>> getObjectsList() {
    if (mediator.getValue() == null) {
        mediator.addSource(dataManager.getObjectsListFromDatabase(), new Observer<List<Foo>>() {
            @Override
            public void onChanged(@Nullable List<Foo> foo) {
                mediator.setValue(foo);
            }
        });
    }
    return mediator;
}
```

## Licence
This project is released under the MIT license.
See [LICENSE](./LICENSE.md) for details.
