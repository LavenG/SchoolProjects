const mongoose = require('mongoose');
const URLSlugs = require('mongoose-url-slugs');

// my schema goes here!
const Comment = new mongoose.Schema({
  text: String,
  user: String
});

const Link = new mongoose.Schema({
  url: String,
  title: String,
  comments: [Comment]
});

//Set the plugin to automatically add the slug
Link.plugin(URLSlugs('title'));

//Define Models
mongoose.model('Comment', Comment);
mongoose.model('Link', Link);

//connect
mongoose.connect('mongodb://localhost/hw05');
