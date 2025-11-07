# Stage 1: Build frontend with npm
FROM node:20 AS build
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source code
COPY . .

# Build project (for production)
RUN npm run build

# Stage 2: Run using Nginx
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Remove default nginx static content
RUN rm -rf ./*

# Copy build result from previous stage
COPY --from=build /app/dist .

# Expose port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]