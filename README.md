# froid-support

<img width="100" height="100" align="middle" src="froid.png"/>

Support library for [froid](https://github.com/mchav/froid).

## Usage and examples

To get/setup froid read the instructions on the [froid Wiki](https://github.com/mchav/froid/wiki).


## Building froid-support

Run `compile` and then `package`. The Frege compiler will attempt to compile some classes before their dependenices. Recompile until there are no errors. I will look into defining the compile order later.

## Contributing

A lot of what there is to do is create the bindings for the other types in `android`. For classes such as adapters/fragments read [this](http://mchav.github.io/functional-inheritance-in-android/) to learn about the design philosophy for subclassing.  Any PRs of this nature are welcome.
